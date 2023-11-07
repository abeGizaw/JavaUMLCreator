package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyDefaultInsnNode;
import domain.MyOpcodes;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMAbstractInsnNodeFactory{

    public MyAbstractInsnNode constructTypedInsnNode(AbstractInsnNode node){
//        check opcodes and create either a MyFieldInsnNode, MyMehtodInsnNode, MyVarInsnNode
        int opcode = node.getOpcode();
//        if(opcode == MyOpcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE ||
//                opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.INVOKEDYNAMIC || opcode == Opcodes.INVOKESTATIC){
//            return new MyASMMethodInsnNode(node);
//        }
        if (opcode == MyOpcodes.PUTFIELD || opcode == MyOpcodes.PUTSTATIC) {
            return new MyASMFieldInsnNode(node);
        }

        return new MyDefaultInsnNode(node);

    }



}
