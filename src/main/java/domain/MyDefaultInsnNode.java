package domain;

public class MyDefaultInsnNode extends MyAbstractInsnNode {
    public MyDefaultInsnNode() {
        super(-1);
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return null;
    }

    @Override
    public int getType() {
        return -1;
    }
}
