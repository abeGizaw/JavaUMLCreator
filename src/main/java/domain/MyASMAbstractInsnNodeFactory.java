package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMAbstractInsnNodeFactory {
    public MyAbstractInsnNode constructTypedInsnNode(AbstractInsnNode node){
        if (node.getType() == MyAbstractInsnNode.LABEL) {
            return new MyASMLabelNode(node);
        } else if (node.getType() == MyAbstractInsnNode.VAR_INSN) {
            return new MyASMVarInsnNode(node);
        }
        return null;
    }
}