package domain.ariscode;

public abstract class MyAbstractInsnNode {
    public static int LABEL;
// ??? What fields are supposed to be here and are these methods supposed to be abstract?

    MyAbstractInsnNode(int l){
        LABEL = l;
    }

    abstract public MyAbstractInsnNode getNext() ;
    abstract public int getOpcode() ;
    public abstract int getType();
}
