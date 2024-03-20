package domain.diagramconverter;

import domain.MyClassNode;
import domain.MyFieldNode;
import domain.MyMethodNode;

import java.util.List;

public class ClassMethodsUMLConverter extends UMLConverterBase{

    @Override
    public String convert(MyClassNode myClassNode, RelationsManager relationsManager) {
        List<MyMethodNode> methods = myClassNode.methods;
        String cleanClassName = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);

        StringBuilder methodString = new StringBuilder();
        for(MyMethodNode method: methods){
            if (methodIsUserGenerated(method)) {
                String accessModifier = getAccessModifier(method.access);
                String nonAccessModifier = getNonAccessModifiers(method.access);
                methodString.append(accessModifier).append(nonAccessModifier);

                String methodName = method.name.equals("<init>") ? cleanClassName : method.name;

                String methodInfo = method.signature == null ?
                        getMethodInfo(method.desc, method) :
                        getMethodInfo(method.signature, method);

                methodString.append(methodName).append(methodInfo).append("\n\t");
            }
        }


        methodString.append("}\n");
        return methodString.toString();
    }
}
