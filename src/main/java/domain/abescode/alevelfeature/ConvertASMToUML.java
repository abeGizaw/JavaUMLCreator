package domain.abescode.alevelfeature;
import domain.MyClassNode;
import domain.MyFieldNode;
import domain.MyMethodNode;
import domain.MyOpcodes;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static presentation.ANSIColors.*;

public class ConvertASMToUML {
    public void run(MyClassNode classNode, StringBuilder pumlContent) {
        generateUmlDiagram(classNode, pumlContent);
    }

    private void generateUmlDiagram(MyClassNode myClassNode, StringBuilder pumlContent) {
        pumlContent.append(convertClassInfo(myClassNode));
        pumlContent.append("{\n");

        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);
        pumlContent.append(convertClassFields(myClassNode.fields));
        pumlContent.append(convertClassMethods(myClassNode.methods, className));

        pumlContent.append("}\n");
    }

    private String convertClassInfo(MyClassNode myClassNode){
        StringBuilder classString = new StringBuilder();

        String classType = getClassType(myClassNode.access);
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);

        if (classType.equals("enum")) {
            classString.append(classType).append(" ").append(className);
        } else {
            String classModifier = getAccessModifier(myClassNode.access);
            classString.append(classModifier).append(classType).append(" ").append(className);
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
            String accessModifier = getAccessModifier(method.access);
            methodString.append(accessModifier);
            if ((method.access & MyOpcodes.ACC_ABSTRACT) != 0) {
                methodString.append("{abstract}");
            }
            String methodName = method.name.equals("<init>") ? className : method.name;
            String descName = getMethodInfo(method.desc);
            methodString.append(methodName).append("():").append(descName).append("\n");
        }
        return methodString.toString();
    }

    private String getMethodInfo(String desc) {
        return "";
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

    private void appendFieldInfo(StringBuilder fieldString, MyFieldNode field) {
        if (isSynthetic(field.access)) {
            return;
        }

        if ((field.access & MyOpcodes.ACC_ENUM) != 0 && (field.access & MyOpcodes.ACC_STATIC) != 0) {
            fieldString.append(field.name).append("\n");
            return;
        }

        String accessModifier = getAccessModifier(field.access);
        String descName = (field.signature != null) ? getFieldType(field.signature) : getFieldType(field.desc);
        fieldString.append(accessModifier).append(" ").append(field.name).append(": ").append(descName).append("\n");
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

        if ((access & MyOpcodes.ACC_STATIC) != 0) {
            modifiers.append("{static}");
        }
        if ((access & MyOpcodes.ACC_FINAL) != 0) {
            modifiers.append("{final}");
        }
        return modifiers.toString();
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
}
