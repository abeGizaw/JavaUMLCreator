package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyDefaultInsnNode;
import domain.MyOpcodes;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MyASMAbstractInsnNodeFactory{

    public MyAbstractInsnNode constructTypedInsnNode(AbstractInsnNode node){
//        check opcodes and create either a MyFieldInsnNode, MyMehtodInsnNode, MyVarInsnNode
        if (node instanceof MethodInsnNode) {
            return new MyASMMethodInsnNode(node);
        }

        return new MyDefaultInsnNode(node);

    }



}
