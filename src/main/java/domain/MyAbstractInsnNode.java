package domain;

public abstract class MyAbstractInsnNode {
    public static final int FIELD_INSN = 4;
    public static final int LABEL = 8;
    public static final int METHOD_INSN = 5;
    public static final int VAR_INSN = 2;

    public abstract MyAbstractInsnNode getNext();
    
    public abstract int getOpcode();

    public abstract int getType();

}
