package domain;

public abstract class MyLabelNode extends MyAbstractInsnNode {
    public MyLabelNode(int opcode) {
        super(opcode);
    }

    public abstract MyLabel getLabel();
}
