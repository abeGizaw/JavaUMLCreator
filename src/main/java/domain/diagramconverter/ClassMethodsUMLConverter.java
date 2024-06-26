package domain.diagramconverter;

import domain.*;
import presentation.ANSIColors;

import java.util.ArrayList;
import java.util.List;

public class ClassMethodsUMLConverter extends UMLConverterBase{
    private String className;
    private String cleanedClassName;

    @Override
    public String convert(MyClassNode myClassNode, RelationsManager relationsManager) {
        List<MyMethodNode> methods = myClassNode.methods;
        setNames(myClassNode.name);

        StringBuilder methodString = new StringBuilder();
        for(MyMethodNode method: methods){
            if (methodIsUserGenerated(method)) {
                String accessModifier = getAccessModifier(method.access);
                String nonAccessModifier = getNonAccessModifiers(method.access);
                methodString.append(accessModifier).append(nonAccessModifier);

                String methodName = method.name.equals("<init>") ? this.cleanedClassName : method.name;

                String methodInfo = method.signature == null ?
                        getMethodInfo(method.desc, method, relationsManager) :
                        getMethodInfo(method.signature, method, relationsManager);

                methodString.append(methodName).append(methodInfo).append("\n\t");
            }
        }


        methodString.append("}\n");
        return methodString.toString();
    }

    private void setNames(String name) {
        this.className = name;
        this.cleanedClassName = cleanClassName(name);
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

    /**
     * @param desc             format:
     *                         ()V for void methods with no params
     *                         (Ljava/lang/String;)V params are passed within the ()
     * @param methodNode       MyASM methodNode
     * @param relationsManager Passed along to analyzeForParams
     * @return UML method declaration
     */
    private String getMethodInfo(String desc, MyMethodNode methodNode, RelationsManager relationsManager) {
        int startParams = desc.indexOf('(');
        int endParams = desc.indexOf(')');
        String returnDesc = desc.substring(endParams+ 1);

        List<String> params = new ArrayList<>();
        generateListOfParams(desc.substring(startParams + 1, endParams), params);
        List<String> paramNames = getParameterNames(methodNode, params);

        String parsedParams = analyzeForParams(params, paramNames, relationsManager);
        String returnType = getFieldType(returnDesc);

        handleDependencyRelation(returnDesc, returnType, relationsManager);


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
     *
     * @param paramInfo        example: [I, Ljava/lang/String;, D]
     * @param paramNames       example: [a, b, c]
     * @param relationsManager passed along to appendParamInfo
     * @return example: a:int, b:String, c:double
     */
    private String analyzeForParams(List<String> paramInfo, List<String> paramNames, RelationsManager relationsManager) {
        if (paramInfo.isEmpty()) {
            return "";
        }
        StringBuilder paramsBuilder = new StringBuilder();

        for (int i = 0; i < paramInfo.size(); i++) {
            String parameterName = paramNames.get(i);
            appendParamInfo(paramsBuilder, paramInfo.get(i), parameterName, relationsManager);
            if (i < paramInfo.size() - 1) {
                paramsBuilder.append(", ");
            }
        }

        return paramsBuilder.toString();
    }

    /**
     * @param paramsBuilder    String of built param list. Example: a:int, b:double,
     * @param param            type param to be added example: Ljava/lang/String;
     * @param parameterName    name of param being added: param name s
     *                         <p>
     *                         ensures: paramsBuilder -> a:int, b:double, s:String (using above example)
     * @param relationsManager adds depends on relations for user made classes
     */
    private void appendParamInfo(StringBuilder paramsBuilder, String param, String parameterName, RelationsManager relationsManager) {
        String fieldType = getFieldType(param);

        handleDependencyRelation(param, fieldType, relationsManager);

        paramsBuilder.append(parameterName).append(":").append(fieldType);
    }

    private void handleDependencyRelation(String objectDesc, String objType, RelationsManager relationsManager) {
        if(!isJavaAPIClass(objectDesc, this.className)){
            relationsManager.addDependsOnARelationShip(this.cleanedClassName, removeBracketsFromDesc(objType));
        }
    }
}
