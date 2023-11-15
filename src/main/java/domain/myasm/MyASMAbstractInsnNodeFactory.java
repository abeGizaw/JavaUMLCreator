package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyDefaultInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMAbstractInsnNodeFactory {
    public MyAbstractInsnNode constructTypedInsnNode(AbstractInsnNode node) {
        if (node == null) {
            return null;
        }
        int type = node.getType();
        if (type == MyAbstractInsnNode.FIELD_INSN) {
            return new MyASMFieldInsnNode(node, new MyASMAbstractInsnNodeFactory());
        } else if (type == MyAbstractInsnNode.LABEL) {
            return new MyASMLabelNode(node, new MyASMAbstractInsnNodeFactory());
        } else if (type == MyAbstractInsnNode.METHOD_INSN) {
            return new MyASMMethodInsnNode(node, new MyASMAbstractInsnNodeFactory());
        } else if (type == MyAbstractInsnNode.VAR_INSN) {
            return new MyASMVarInsnNode(node, new MyASMAbstractInsnNodeFactory());
        }
        return new MyDefaultInsnNode();
    }
}
