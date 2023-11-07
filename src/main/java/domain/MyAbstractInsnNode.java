package domain;

public abstract class MyAbstractInsnNode {
    public static int LABEL;

    public void setLABEL(int l) {
        LABEL = l;
    }

    abstract public MyAbstractInsnNode getNext();

    abstract public int getOpcode();

    abstract public int getType();
}
