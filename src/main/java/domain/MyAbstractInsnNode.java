package domain;

public abstract class MyAbstractInsnNode {
    public static final int LABEL = 8;

    protected int opcode;

    public MyAbstractInsnNode(int opcode) {
        this.opcode = opcode;
    }

    public abstract MyAbstractInsnNode getNext();
    public int getOpcode() {
        return opcode;
    }
    public abstract int getType();
}
