package domain;

public class DefaultInsnNode extends MyAbstractInsnNode {
    public DefaultInsnNode() {
        super(-1);
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return null;
    }

    @Override
    public MyAbstractInsnNode getPrevious() {
        return null;
    }

    @Override
    public int getType() {
        return -1;
    }
}
