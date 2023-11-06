package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMAbstractInsnNodeFactory {
    public MyAbstractInsnNode constructTypedInsnNode(AbstractInsnNode node){
        if (node.getType() == MyAbstractInsnNode.LABEL) {
            return new MyASMLabelNode(node);
        }
        return null;
    }
}