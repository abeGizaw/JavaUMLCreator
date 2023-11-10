package domain.checks;

import domain.*;
import java.util.ArrayList;
import java.util.List;

public class TemplateMethodPattern implements Check{
    public List<Message> run(MyClassNode myClassNode){
        return detectTemplateMethod(myClassNode);
    }

    private List<Message> detectTemplateMethod(MyClassNode myClassNode) {
        List<Message> usesTemplatePattern = new ArrayList<>();
        if((myClassNode.access & MyOpcodes.ACC_ABSTRACT) != 0){
            for(MyMethodNode method: myClassNode.methods){
                if((method.access & MyOpcodes.ACC_FINAL) != 0 && containsAbstractMethodCall(method.instructions, myClassNode.methods)){
                    String message = "The Class "+ myClassNode.name + " uses the templateMethod Pattern";
                    usesTemplatePattern.add(new Message(LintType.TEMPLATE_METHOD_PATTERN, message, myClassNode.name));
                }
            }
        }
        return usesTemplatePattern;
    }

    private boolean containsAbstractMethodCall(List<MyAbstractInsnNode> instructions, List<MyMethodNode> methods) {
        for(MyAbstractInsnNode node: instructions){
            if(isAMethodCall(node)){
                MyMethodInsnNode methodInsnNode = (MyMethodInsnNode) node;
                for (MyMethodNode methodNode : methods) {
                    if(similarAbstractMethodInsn(methodInsnNode, methodNode)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean similarAbstractMethodInsn(MyMethodInsnNode methodInsnNode, MyMethodNode methodNode) {
        if (methodNode.name.equals(methodInsnNode.name) && methodNode.desc.equals(methodInsnNode.desc)) {
            return (methodNode.access & MyOpcodes.ACC_ABSTRACT) != 0;
        }
        return false;
    }

    private boolean isAMethodCall(MyAbstractInsnNode node) {
        int opcode = node.getOpcode();
        return opcode == MyOpcodes.INVOKEVIRTUAL || opcode == MyOpcodes.INVOKEINTERFACE ||
                opcode == MyOpcodes.INVOKESPECIAL || opcode == MyOpcodes.INVOKEDYNAMIC;
    }

}
