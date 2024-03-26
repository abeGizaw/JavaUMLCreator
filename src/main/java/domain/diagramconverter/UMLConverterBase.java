package domain.diagramconverter;

import domain.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UMLConverterBase implements UMLConverter{

    abstract public String convert(MyClassNode myclassNode, RelationsManager relationsManager);

    /**
     * @param desc examples: V, I, Ljava/lang/String; Ljava/util/List<Ljava/lang/Integer;>;
     * @return The type of desc passed in. example: int, String, List<Integer>
     */
    protected String getFieldType(String desc) {
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

    protected boolean isPrimitive(String desc) {
        return !desc.startsWith("L") && !desc.startsWith("[");
    }

    /**
     *
     * @param desc - 1 char representing the primitive type
     * @return java primitive type
     */
    private String getPrimitiveFieldType(String desc) {
        PrimitiveStringFactory primitiveStringFactory = new PrimitiveStringFactory();
        return primitiveStringFactory.getPrimitiveFieldType(desc);
    }
    protected String getAccessModifier(int access) {
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

    protected String getNonAccessModifiers(int access){
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
    protected boolean isJavaAPIClass(String desc, String originalClassName) {
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

    protected boolean isCollectionType(String descName) {
        return descName.contains("[") || descName.contains("<");
    }


    protected String cleanClassName(String fullClassName){
        return fullClassName.substring(fullClassName.lastIndexOf("/") + 1);
    }

}
