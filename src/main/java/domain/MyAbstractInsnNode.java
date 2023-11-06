package domain;

public abstract class MyAbstractInsnNode {
    public static int LABEL;
// ??? What feilds are supposed to be here and are these methods supposed to be abstract?

    public void setLABEL(int l){
        LABEL = l;
    }

    abstract public MyAbstractInsnNode getNext() ;
    abstract public int getOpcode() ;
    abstract public int getType();
}
