package domain.myasm;

import domain.MyDefaultInsnNode;
import domain.MyAbstractInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMAbstractInsnNodeFactory {
    public MyAbstractInsnNode constructTypedInsnNode(AbstractInsnNode node){
        if (node == null) {
            return null;
        }
        int type = node.getType();
        if (type == MyAbstractInsnNode.FIELD_INSN) {
            return new MyASMFieldInsnNode(node);
        } else if (type == MyAbstractInsnNode.LABEL) {
            return new MyASMLabelNode(node);
        } else if (type == MyAbstractInsnNode.METHOD_INSN) {
            return new MyASMMethodInsnNode(node);
        } else if (type == MyAbstractInsnNode.VAR_INSN) {
            return new MyASMVarInsnNode(node);
        }
        return new MyDefaultInsnNode();
    }
}
