package domain;

public class MyDefaultInsnNode extends MyAbstractInsnNode {
    @Override
    public MyAbstractInsnNode getNext() {
        return null;
    }

    @Override
    public int getOpcode() {
        return -1;
    }

    @Override
    public int getType() {
        return -1;
    }
}
