package domain.diagramconverter;
import domain.*;
import presentation.ANSIColors;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts ASM representation of classes to UML diagrams.
 * This class provides methods to generate UML diagram content from a given set of class nodes,
 * handling the conversion of class information, fields, and methods into UML syntax.
 */
public class ConvertASMToUML implements Diagram{
    private final StringBuilder classUmlContent;
    private final Map<String, Integer> hasARelationShipByClass = new HashMap<>();
    private final Set<String> allHasARelationships = new HashSet<>();

    /**
     * Constructs a new ConvertASMToUML instance with a StringBuilder to hold the UML diagram content.
     *
     * @param classUmlContent The StringBuilder instance that will accumulate the UML diagram content.
     */
    public ConvertASMToUML(StringBuilder classUmlContent){
        this.classUmlContent = classUmlContent;
    }

    /**
     * Generates UML diagram content for a single class node and appends it to the provided StringBuilder.
     * This includes the class's fields, methods, and any relationships identified during conversion.
     *
     * @param myClassNode  The class node to convert into UML diagram content.
     * @param pumlContent  The StringBuilder instance to which the UML content will be appended.
     */
    public void generateDiagramByNode(MyClassNode myClassNode, StringBuilder pumlContent) {
        pumlContent.append(convertClassInfo(myClassNode));
        pumlContent.append("{\n\t");

        String cleanClassName = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);

        pumlContent.append(convertClassFields(myClassNode.fields, myClassNode.name));
        pumlContent.append(convertClassMethods(myClassNode.methods, cleanClassName));

        pumlContent.append("}\n");


        allHasARelationships.addAll(convertKeyNames(hasARelationShipByClass));
        addExtendsAndImplementsRelation(myClassNode, cleanClassName);
        hasARelationShipByClass.clear();
    }

    /**
     * Generates a UML diagram for an entire package, organizing class nodes by package and converting each to UML content.
     * This method also handles the aggregation of relationship information across classes.
     *
     * @param packageToMyClassNode  A map associating package names with lists of MyClassNode instances belonging to each package.
     * @return                      A StringBuilder containing the complete UML diagram content.
     */
    public StringBuilder generateDiagramByPackage(Map<String, List<MyClassNode>> packageToMyClassNode) {
        classUmlContent.append("@startuml\n");
        for(String packageName : packageToMyClassNode.keySet()){
            if (!packageName.isEmpty()) {
                classUmlContent.append("package ").append(packageName).append(" {\n\t");
            }

            List<MyClassNode> nodesForPackage = packageToMyClassNode.get(packageName);
            for (MyClassNode myClassNode : nodesForPackage) {
                generateDiagramByNode(myClassNode, classUmlContent);
                classUmlContent.append("\n");
            }

            if (!packageName.isEmpty()) {
                classUmlContent.append("}\n");
            }
        }
        for(String relationship: allHasARelationships){
            classUmlContent.append(relationship).append("\n");
        }
        classUmlContent.append("@enduml");
        return classUmlContent;
    }

    private void addExtendsAndImplementsRelation(MyClassNode myClassNode, String cleanClassName) {
        // Adds the implement relations
        if(!myClassNode.interfaces.isEmpty()){
            for(String classInterface : myClassNode.interfaces){
                if(!classInterface.startsWith("java")){
                    String interfaceName = classInterface.substring(classInterface.lastIndexOf('/') + 1);
                    allHasARelationships.add(cleanClassName + "..|>" + interfaceName);
                }
            }
        }

        // Adds the extends relations
        String abstractClass = myClassNode.superName;
        if(!abstractClass.isEmpty()){
            if(!abstractClass.startsWith("java")){
                String abstractClassName = abstractClass.substring(abstractClass.lastIndexOf('/') + 1);
                allHasARelationships.add(cleanClassName + "--|>" + abstractClassName);
            }
        }
    }

    private Set<String> convertKeyNames(Map<String, Integer> hasARelMap) {
        Set<String> results = new HashSet<>();
        for(String relation: hasARelMap.keySet()){
            if(hasARelMap.get(relation) == 1){
                results.add(relation);
            } else {
                int count = hasARelMap.get(relation);
                String relWithNum = relation.substring(0, relation.indexOf('>') + 1)
                                    + "\"" + count + "\"" +
                                    relation.substring(relation.indexOf('>') + 1);

                results.add(relWithNum);
            }
        }
        return results;
    }


    /**
     * Converts the information of a MyClassNode object's class type into a UML formatted string.
     * @param myClassNode The class node containing the information to convert.
     * @return A string representing the class in UML format.
     */
    private String convertClassInfo(MyClassNode myClassNode){
        StringBuilder classString = new StringBuilder();

        String classType = getClassType(myClassNode.access);

        if(myClassNode.name.contains("$")){
            convertInnerClassInfo(myClassNode, classString, classType);
        } else {
            convertOuterClassInfo(myClassNode, classString, classType);
        }

        return classString.toString();
    }


    private String convertClassFields(List<MyFieldNode> fields, String className) {
        StringBuilder fieldString = new StringBuilder();
        for(MyFieldNode field: fields){
            appendFieldInfo(fieldString, field, className);
        }

        return fieldString.toString();
    }

    private String convertClassMethods(List<MyMethodNode> methods, String className) {
        StringBuilder methodString = new StringBuilder();
        for(MyMethodNode method: methods){
            if (methodIsUserGenerated(method)) {
                String accessModifier = getAccessModifier(method.access);
                String nonAccessModifier = getNonAccessModifiers(method.access);
                methodString.append(accessModifier).append(nonAccessModifier);

                String methodName = method.name.equals("<init>") ? className : method.name;

                String methodInfo = method.signature == null ?
                        getMethodInfo(method.desc, method) :
                        getMethodInfo(method.signature, method);

                methodString.append(methodName).append(methodInfo).append("\n\t");
            }
        }
        return methodString.toString();
    }

    /**
     * Determines the UML type based on the access flags.
     *
     * @param access Access flags of the class.
     * @return A string representing the UML type.
     */
    private String getClassType(int access) {
        if((access & MyOpcodes.ACC_INTERFACE) != 0){
            return "interface";
        } else if((access & MyOpcodes.ACC_ABSTRACT) != 0){
            return "abstract class";
        } else if((access & MyOpcodes.ACC_ENUM) != 0){
            return "enum";
        } else {
            return "class";
        }
    }

    /**
     * Generates UML information for non-inner (outer) classes.
     *
     * @param myClassNode MyClassNode representing the class.
     * @param classString StringBuilder to append the class information.
     * @param classType The type of class (e.g., class, abstract class, interface).
     */
    private void convertOuterClassInfo(MyClassNode myClassNode, StringBuilder classString, String classType) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);
        if (classType.equals("enum")) {
            classString.append(classType).append(" ").append(className);
        } else {
            String classModifier = getAccessModifier(myClassNode.access);
            classString.append(classModifier).append(classType).append(" ").append(className);
        }
    }


    /**
     * Generates UML information for inner classes.
     *
     * @param myClassNode MyClassNode representing the class.
     * @param classString StringBuilder to append the class information.
     * @param classType The type of class (e.g., class, abstract class, interface).
     */
    private void convertInnerClassInfo(MyClassNode myClassNode, StringBuilder classString, String classType) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("$") + 1);
        MyInnerClassNode innerClassNode = findInnerClassNode(myClassNode, myClassNode.name);
        if(innerClassNode != null){
            String classModifier = getAccessModifier(innerClassNode.access);
            classString.append(classModifier).append(classType).append(" ").append(className);
        }
    }


    private MyInnerClassNode findInnerClassNode(MyClassNode myClassNode, String name) {
        for (MyInnerClassNode icn : myClassNode.innerClasses) {
            if (icn.name.equals(name)) {
                return icn;
            }
        }
        return null;
    }

    private boolean methodIsUserGenerated(MyMethodNode method) {
        if((method.access & MyOpcodes.ACC_SYNTHETIC) != 0 || method.name.startsWith("lambda$")){
            return false;
        }
        //inner class
        if (method.name.contains("$")) {
            return false;
        }
        return !method.name.equals("<clinit>");
    }

    private void appendFieldInfo(StringBuilder fieldString, MyFieldNode field, String className) {
        if (isSynthetic(field.access)) {
            return;
        }

        if ((field.access & MyOpcodes.ACC_ENUM) != 0 && (field.access & MyOpcodes.ACC_STATIC) != 0) {
            fieldString.append(field.name).append("\n\t");
            return;
        }

        String accessModifier = getAccessModifier(field.access);
        String nonAccessModifier = getNonAccessModifiers(field.access);
        fieldString.append(accessModifier).append(nonAccessModifier);

        String fullDesc = (field.signature != null) ? field.signature : field.desc;
        String descName = getFieldType(fullDesc);


        if(!isJavaAPIClass(fullDesc, className)){
            String cleanedDescName = removeBracketsFromDesc(descName);
            addAHasARelationship(cleanedDescName, className, isCollectionType(descName));
        }
        fieldString.append(" ").append(field.name).append(": ").append(descName).append("\n\t");
    }

    /**
     *
     * @param descName exmaple: Set<MockAbstract>
     *                          HashMap<MockEnum,MockInterface>
     *                          AccessModifierVariety[]
     * @return example: MockAbstract
     *                  MockEnum,MockInterface
     *                  AccessModifierVariety
     */
    private String removeBracketsFromDesc(String descName) {
        if(descName.endsWith("[]")){
            return removeBracketsFromDesc(descName.substring(0, descName.length() - 2));
        } else if(descName.contains("<")){
            while(descName.contains("<")){
                 descName = descName.substring(descName.indexOf("<") + 1);
            }
            descName = descName.substring(0, descName.indexOf(">"));
            descName = descName.replace("[]", "");
        }

        return descName;
    }

    private void addAHasARelationship(String descName, String className, boolean collectionType) {
        for (String field: descName.split(",")){
            String cleanClassName = className.substring(className.lastIndexOf("/") + 1);
            String baseRelationShip = cleanClassName + "-->";
            String relationship = baseRelationShip + field;
            String multipleRelationship = baseRelationShip + "\"*\"" + field;

            if (collectionType) {
                hasARelationShipByClass.remove(relationship);
                hasARelationShipByClass.putIfAbsent(multipleRelationship, 1);
            } else {
                if (!hasARelationShipByClass.containsKey(multipleRelationship)) {
                    if (hasARelationShipByClass.containsKey(relationship)) {
                        hasARelationShipByClass.put(relationship, hasARelationShipByClass.get(relationship) + 1);
                    } else {
                        hasARelationShipByClass.put(relationship, 1);
                    }

                }
            }
        }
    }

    private boolean isCollectionType(String descName) {
        return descName.contains("[") || descName.contains("<");
    }


    /**
     * @param desc format:
     *     ()V for void methods with no params
     *     (Ljava/lang/String;)V params are passed within the ()
     * @param methodNode MyASM methodNode
     * @return UML method declaration
     */
    private String getMethodInfo(String desc, MyMethodNode methodNode) {
        int startParams = desc.indexOf('(');
        int endParams = desc.indexOf(')');

        List<String> params = new ArrayList<>();
        generateListOfParams(desc.substring(startParams + 1, endParams), params);
        List<String> paramNames = getParameterNames(methodNode, params);

        String parsedParams = analyzeForParams(params, paramNames);
        String returnType = getFieldType(desc.substring(endParams+ 1));

        return "(" + parsedParams + "):" + returnType;
    }

    /**
     *
     * @param methodNode - passed in to check the local variables (parameters) in method
     * @param paramInfo - example format: [I, Ljava/lang/String;, D]
     * @return will return names of params as a list. Example: [a, b, c]
     */
    private List<String> getParameterNames(MyMethodNode methodNode, List<String> paramInfo) {
        List<String> paramNames = new ArrayList<>();


        int startLocalIndex = (methodNode.access & MyOpcodes.ACC_STATIC) != 0 && methodNode.localVariables != null? 0 : 1;


        if (methodNode.localVariables != null) {
            for (int i = 0; i < paramInfo.size(); i++) {
                MyLocalVariableNode lvNode = methodNode.localVariables.get(i + startLocalIndex);
                paramNames.add(lvNode.name);
            }
        } else {
            for (int i = 0; i < paramInfo.size(); i++) {
                paramNames.add("param" + (i + 1));
            }
        }

        return paramNames;
    }

    /**
     *
     * @param desc example format: ILjava/lang/String;D -> int String double
     * @param params an emtpy list to hold params: For above example: [I, Ljava/lang/String;, D]
     */
    private void generateListOfParams(String desc, List<String> params) {
        if (desc.isEmpty()) {
            return;
        }

        int startIndex =0;

        while (startIndex < desc.length()) {
            char startChar = desc.charAt(startIndex);
            if (isPrimitive(String.valueOf(startChar))) {
                params.add(String.valueOf(startChar));
                startIndex ++;
            } else if (startChar == 'L') {
                String javaObjDesc = processObjectDescriptor(desc.substring(startIndex));
                params.add(javaObjDesc);
                startIndex += javaObjDesc.length();
            } else if (startChar == '[') {
                String arrayDesc = processArrayDescriptor(desc.substring(startIndex));
                params.add(arrayDesc);
                startIndex += arrayDesc.length();
            }
        }
    }

    private String processObjectDescriptor(String desc) {
        int nestingLevel = 0;
        int index = 0;

        while (index < desc.length()) {
            char currentChar = desc.charAt(index);
            if (currentChar == '<') {
                nestingLevel++;
            } else if (currentChar == '>') {
                nestingLevel--;
            } else if (currentChar == ';' && nestingLevel == 0) {
                break;
            }
            index++;
        }
        index++;
        return desc.substring(0, index);
    }

    private String processArrayDescriptor(String desc) {
        int index = 0;
        while (desc.charAt(index) == '[') {
            index++;
        }
        if (desc.charAt(index) == 'L') {
            String objectPart = processObjectDescriptor(desc.substring(index));
            return desc.substring(0, index) + objectPart;
        } else {
            return desc.substring(0, index + 1);
        }
    }

    /**
     * Helps of mapping from type to name
     * @param paramInfo example: [I, Ljava/lang/String;, D]
     * @param paramNames example: [a, b, c]
     * @return example: a:int, b:String, c:double
     */
    private String analyzeForParams(List<String> paramInfo, List<String> paramNames) {
        if (paramInfo.isEmpty()) {
            return "";
        }
        StringBuilder paramsBuilder = new StringBuilder();

        for (int i = 0; i < paramInfo.size(); i++) {
            String parameterName = paramNames.get(i);
            appendParamInfo(paramsBuilder, paramInfo.get(i), parameterName);
            if (i < paramInfo.size() - 1) {
                paramsBuilder.append(", ");
            }
        }

        return paramsBuilder.toString();
    }

    /**
     *
     * @param paramsBuilder String of built param list. Example: a:int, b:double,
     * @param param type param to be added example: Ljava/lang/String;
     * @param parameterName name of param being added: param name s
     *
     * ensures: paramsBuilder -> a:int, b:double, s:String (using above example)
     */
    private void appendParamInfo(StringBuilder paramsBuilder, String param, String parameterName) {
        String fieldType = getFieldType(param);
        paramsBuilder.append(parameterName).append(":").append(fieldType);
    }

    /**
     * @param desc examples: V, I, Ljava/lang/String; Ljava/util/List<Ljava/lang/Integer;>;
     * @return The type of desc passed in. example: int, String, List<Integer>
     */
    private String getFieldType(String desc) {
        if (desc.startsWith("[")) {
            return getFieldType(desc.substring(1)) + "[]";
        } else if (desc.startsWith("L") && desc.endsWith(";")) {
            if (desc.contains("<")) {
                return getCollectionType(desc);
            } else {
                return desc.substring(desc.lastIndexOf('/') + 1, desc.length() - 1);
            }
        } else {
            return getPrimitiveFieldType(desc);
        }
    }

    /**
     *
     * @param desc collections.
     *             Examples: Ljava/util/List<Ljava/lang/Integer;>;
     *                      Ljava/util/Map<Ljava/lang/String;Ljava/util/List<Ljava/lang/Double;>;>;
     * <p>
     * Matcher looking for java.util.regex.Matcher[pattern=.*?/(\w+)<.* region=0,x lastmatch=]
     *
     * @return desc in UML collection format (Java format)
     *         Examples: List<Integer>
     *                   Map<String,List<Double>>
     */
    private String getCollectionType(String desc) {
        Pattern pattern = Pattern.compile(".*?/(\\w+)<.*");
        Matcher matcher = pattern.matcher(desc);

        if(matcher.find()){
            String collectionType = matcher.group(1);
            int collectionParamsIndex = desc.indexOf(collectionType) + collectionType.length() + 1;
            String collectedTypes = getCollectionHoldTypes(desc.substring(collectionParamsIndex));

            return collectionType + "<" + collectedTypes + ">";
        }

        return desc;
    }

    /**
     *
     * @param collected Java type in a collection.
     *                  Example: Ljava/lang/Character;>;
     *                           Ljava/lang/String;Ljava/util/List<Ljava/lang/Double;>;>;
     * @return Examples: Integer
     *                   Map<String,List<Double>>
     */
    private String getCollectionHoldTypes(String collected) {
        List<String> collectionHoldTypeList = cleanCollectionParsing(parseGenericTypes(collected));
        return generateCollectedTypes(collectionHoldTypeList);
    }

    /**
     *
     * @param collectionTypeList Examples: [Ljava/lang/Integer;]
     *                                     [Ljava/lang/String;, Ljava/util/List<Ljava/lang/Double;>;]
     * @return Examples: Integer
     *                   Map<String,List<Double>>
     */
    private String generateCollectedTypes(List<String> collectionTypeList) {
        StringBuilder s= new StringBuilder();
        for(int i = 0; i < collectionTypeList.size(); i ++){
            String fieldType = getFieldType(collectionTypeList.get(i));
            s.append(fieldType);
            if(i != collectionTypeList.size() - 1){
                s.append(",");
            }
        }
        return s.toString();
    }

    /**
     *
     * @param originalList cleans the output of @parseGenericTypes when needed
     * @return [Ljava/lang/Integer, >;] -> [Ljava/lang/Integer;]
     *         [Ljava/lang/String, Ljava/util/List<Ljava/lang/Double;>, >;] -> [Ljava/lang/String;, Ljava/util/List<Ljava/lang/Double;>;]
     */
    private List<String> cleanCollectionParsing(List<String> originalList){
        List<String> modifiedList = new ArrayList<>();
        for (String entry : originalList) {
            if (entry.equals(">;")) {
                break;
            } else {
                modifiedList.add(entry + ";");
            }
        }
        return modifiedList;
    }

    /**
     *
     * @param innerTypes Takes a generic type and tries to separate in a list
     *                   Example: Ljava/lang/Character;>;
     *                            Ljava/lang/String;Ljava/util/List<Ljava/lang/Double;>;>;
     * @return Example: [Ljava/lang/Integer, >;]
     *                  [Ljava/lang/String, Ljava/util/List<Ljava/lang/Double;>, >;]
     */
    private List<String> parseGenericTypes(String innerTypes) {
        List<String> types = new ArrayList<>();
        int level = 0;
        StringBuilder currentType = new StringBuilder();

        for (int i = 0; i < innerTypes.length(); i++) {
            char currentChar = innerTypes.charAt(i);
            if (currentChar == '<') {
                level++;
                currentType.append(currentChar);
            } else if (currentChar == '>') {
                level--;
                currentType.append(currentChar);
            } else if (currentChar == ';' && level == 0) {
                types.add(currentType.toString());
                currentType.setLength(0);
            } else {
                currentType.append(currentChar);
            }
        }
        if (currentType.length() > 0) {
            types.add(currentType.toString());
        }

        return types;
    }


    private boolean isSynthetic(int access) {
        return (access & MyOpcodes.ACC_SYNTHETIC) != 0;
    }
    private boolean isPrimitive(String desc) {
        return !desc.startsWith("L") && !desc.startsWith("[");
    }

    /**
     *
     * @param desc - 1 char representing the primitive type
     * @return java primitive type
     */
    private String getPrimitiveFieldType(String desc) {
        switch(desc){
            case "B":
                return "byte";
            case "C":
                return "char";
            case "D":
                return "double";
            case "F":
                return "float";
            case "I":
                return "int";
            case "J":
                return "long";
            case "S":
                return "short";
            case "Z":
                return "boolean";
            case "V":
                return "void";
            default:
                return null;
        }
    }
    private String getAccessModifier(int access) {
        StringBuilder modifiers = new StringBuilder();

        if ((access & MyOpcodes.ACC_PUBLIC) != 0) {
            modifiers.append("+");
        } else if ((access & MyOpcodes.ACC_PROTECTED) != 0) {
            modifiers.append("#");
        } else if ((access & MyOpcodes.ACC_PRIVATE) != 0) {
            modifiers.append("-");
        } else{
            modifiers.append("~");
        }
        return modifiers.toString();
    }

    private String getNonAccessModifiers(int access){
        StringBuilder modifiers = new StringBuilder();
        if ((access & MyOpcodes.ACC_STATIC) != 0) {
            modifiers.append("{static}");
        }
        if ((access & MyOpcodes.ACC_FINAL) != 0) {
            modifiers.append("{final}");
        }
        if((access & MyOpcodes.ACC_ABSTRACT) != 0 && (access & MyOpcodes.ACC_INTERFACE) == 0){
            modifiers.append("{abstract}");
        }
        return modifiers.toString();
    }

    /**
     *
     * @param desc format: Ljava/util/List<Ljava/lang/Integer;>;
     *                     [Ldomain/diagramconverter/ClassUmlMockTestClasses/CollectionFieldsConverter;
     *                     Ldomain/diagramconverter/ClassUmlMockTestClasses/ArrayFieldsConverter;
     *
     * @param originalClassName format: <pathFromPackage>/<classname>
     *                                  domain/diagramconverter/ClassUmlMockTestClasses/HasATest
     * @return T/F if it's a javaAPIclass
     */
    private boolean isJavaAPIClass(String desc, String originalClassName) {
        if(isPrimitive(desc)){
            return true;
        }

        String classNameDirectory = originalClassName.substring(0, originalClassName.lastIndexOf('/'));
        if (isCollectionType(desc)) {
            if(desc.startsWith("[")){
                return isJavaAPIClass(desc.substring(1), originalClassName);
            } else if (desc.contains(classNameDirectory)) {
                return false;
            }
        }

        String fieldClassName = desc.substring(1, desc.length() - 1);
        return fieldClassName.startsWith("java/");
    }

}
