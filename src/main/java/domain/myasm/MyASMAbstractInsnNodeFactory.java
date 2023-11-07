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
        int type = node.getType();
        if (type  == AbstractInsnNode.METHOD_INSN) {
            return new MyASMMethodInsnNode(node);
        }

        return new MyDefaultInsnNode(node);
    }
}
