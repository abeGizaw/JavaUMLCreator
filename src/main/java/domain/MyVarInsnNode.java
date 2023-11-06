package domain;

public abstract class MyVarInsnNode extends MyAbstractInsnNode {
    public int var;

    public MyVarInsnNode(int opcode) {
        super(opcode);
    }
}
