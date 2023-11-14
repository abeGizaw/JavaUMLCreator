package domain.diagramconverter;
import domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static presentation.ANSIColors.*;

public class ConvertASMToUML implements Diagram{
    private final StringBuilder classUmlContent;
    public ConvertASMToUML(StringBuilder classUmlContent){
        this.classUmlContent = classUmlContent;

    }

    public void generateDiagramByNode(MyClassNode myClassNode, StringBuilder pumlContent) {
        pumlContent.append(convertClassInfo(myClassNode));
        pumlContent.append("{\n\t");

        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);
        pumlContent.append(convertClassFields(myClassNode.fields));
        pumlContent.append(convertClassMethods(myClassNode.methods, className));

        pumlContent.append("}\n");
    }

    @Override
    //will be the one I want to use once fully working
    public StringBuilder generateDiagram(List<MyClassNode> myClassNodeList) {
        classUmlContent.append("@startuml\n");
        for(MyClassNode classNode: myClassNodeList){
            generateDiagramByNode(classNode, classUmlContent);
            classUmlContent.append("\n");
        }
        classUmlContent.append("@enduml");
        return classUmlContent;
    }



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


    private String convertClassFields(List<MyFieldNode> fields) {
        StringBuilder fieldString = new StringBuilder();
        for(MyFieldNode field: fields){
            appendFieldInfo(fieldString, field);
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

    private void convertOuterClassInfo(MyClassNode myClassNode, StringBuilder classString, String classType) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);
        if (classType.equals("enum")) {
            classString.append(classType).append(" ").append(className);
        } else {
            String classModifier = getAccessModifier(myClassNode.access);
            classString.append(classModifier).append(classType).append(" ").append(className);
        }
    }


    private void convertInnerClassInfo(MyClassNode myClassNode, StringBuilder classString, String classType) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("$") + 1);
        MyInnerClassNode innerClassNode = findInnerClassNode(myClassNode, myClassNode.name);
        if(innerClassNode != null){
            String classModifier = getAccessModifier(innerClassNode.access);
            classString.append(classModifier).append(classType).append(" ").append(className);
        }
    }
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

    private void appendFieldInfo(StringBuilder fieldString, MyFieldNode field) {
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


        String descName = (field.signature != null) ? getFieldType(field.signature) : getFieldType(field.desc);
        fieldString.append(" ").append(field.name).append(": ").append(descName).append("\n\t");
    }

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

    private void appendParamInfo(StringBuilder paramsBuilder, String param, String parameterName) {
        String fieldType = getFieldType(param);
        paramsBuilder.append(parameterName).append(":").append(fieldType);
    }

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

    private String getCollectionType(String desc) {
        Pattern pattern = Pattern.compile(".*?/(\\w+)<.*");
        Matcher matcher = pattern.matcher(desc);
        if(matcher.find()){
            String collectionType = matcher.group(1);
            String[] collectionHoldTypes = desc.substring(desc.indexOf("<") + 1, desc.lastIndexOf(">")).split(";");
            String collectedTypes = Arrays.stream(collectionHoldTypes)
                    .map(typeDesc -> getFieldType(typeDesc + (typeDesc.contains("<") ? ";>;" : ";"))) // Add semicolon back if needed
                    .filter(Objects::nonNull) // Ignore null values
                    .collect(Collectors.joining(", "));

            return collectionType + "<" + collectedTypes + ">";
        }
        return desc;
    }


    private boolean isSynthetic(int access) {
        return (access & MyOpcodes.ACC_SYNTHETIC) != 0;
    }
    private boolean isPrimitive(String desc) {
        return !desc.startsWith("L") && !desc.startsWith("[");
    }
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
}
