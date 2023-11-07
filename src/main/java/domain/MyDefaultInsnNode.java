package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

public class MyDefaultInsnNode extends MyAbstractInsnNode {
    public MyDefaultInsnNode(AbstractInsnNode node) {
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return null;
    }

    @Override
    public int getOpcode() {
        return 0;
    }

    @Override
    public int getType() {
        return 0;
    }

    // Any other methods relevant to your default node
}
