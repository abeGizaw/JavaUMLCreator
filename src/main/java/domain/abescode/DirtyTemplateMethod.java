package domain.abescode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import java.util.List;
import static presentation.ANSIColors.*;

public class DirtyTemplateMethod {
    public void run(ClassNode myClassNode){
        boolean usesTemplate = detectTemplateMethod(myClassNode);
        if(usesTemplate){
            System.out.println(YELLOW + "The Class "+ myClassNode.name + " uses the templateMethod Pattern" + RESET);
        } else {
            System.out.println(YELLOW + "The Class "+ myClassNode.name + " does not use the templateMethod Pattern" + RESET);
        }
    }

    private boolean detectTemplateMethod(ClassNode myClassNode) {
        if((myClassNode.access & Opcodes.ACC_ABSTRACT) == 0){
            return false;
        }
        for(MethodNode method: myClassNode.methods){
            if((method.access & Opcodes.ACC_FINAL) != 0 && containsAbstractMethodCall(method.instructions, myClassNode.methods)){
                return true;
            }
        }
        return false;
    }

    private boolean containsAbstractMethodCall(InsnList instructions, List<MethodNode> methods) {
        for(AbstractInsnNode node: instructions){
            if(isAMethodCall(node)){
                MethodInsnNode methodInsnNode = (MethodInsnNode) node;

                for (MethodNode methodNode : methods) {
                    if (methodNode.name.equals(methodInsnNode.name) && methodNode.desc.equals(methodInsnNode.desc)) {
                        if ((methodNode.access & Opcodes.ACC_ABSTRACT) != 0) {
                            return true;
                        }
                    }
                }

            }
        }

        return false;
    }

    private boolean isAMethodCall(AbstractInsnNode node) {
        int opcode = node.getOpcode();
        return opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE ||
                opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.INVOKEDYNAMIC;
    }

}
