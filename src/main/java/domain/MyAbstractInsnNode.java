package domain;

public abstract class MyAbstractInsnNode {
    public static final int LABEL = 8;
    public static final int VAR_INSN = 2;

    protected int opcode;

    public MyAbstractInsnNode(int opcode) {
        this.opcode = opcode;
    }

    public abstract MyAbstractInsnNode getNext();
    public int getOpcode() {
        return opcode;
    }

    public abstract MyAbstractInsnNode getPrevious();
    public abstract int getType();
}
