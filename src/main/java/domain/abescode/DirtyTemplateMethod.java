package domain.abescode;

import domain.*;
import org.objectweb.asm.Opcodes;
import java.util.List;
import static presentation.ANSIColors.*;

public class DirtyTemplateMethod {
    public void run(MyClassNode myClassNode){
        boolean usesTemplate = detectTemplateMethod(myClassNode);
        if(usesTemplate){
            System.out.println(YELLOW + "The Class "+ myClassNode.name + " uses the templateMethod Pattern" + RESET);
        } else {
            System.out.println(YELLOW + "The Class "+ myClassNode.name + " does not use the templateMethod Pattern" + RESET);
        }
    }

    private boolean detectTemplateMethod(MyClassNode myClassNode) {
        if((myClassNode.access & Opcodes.ACC_ABSTRACT) == 0){
            return false;
        }
        for(MyMethodNode method: myClassNode.methods){
            if((method.access & MyOpcodes.ACC_FINAL) != 0 && containsAbstractMethodCall(method.instructions, myClassNode.methods)){
                return true;
            }
        }
        return false;
    }

    private boolean containsAbstractMethodCall(List<MyAbstractInsnNode> instructions, List<MyMethodNode> methods) {
        for(MyAbstractInsnNode node: instructions){
            if(isAMethodCall(node)){
                MyMethodInsnNode methodInsnNode = (MyMethodInsnNode) node;
                for (MyMethodNode methodNode : methods) {
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

    private boolean isAMethodCall(MyAbstractInsnNode node) {
        int opcode = node.getOpcode();
        return opcode == MyOpcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE ||
                opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.INVOKEDYNAMIC;
    }

}
