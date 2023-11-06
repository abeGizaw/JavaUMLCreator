package domain;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MyASMVarInsnNode extends MyVarInsnNode {
    private VarInsnNode varInsnNode;

    public MyASMVarInsnNode(VarInsnNode varInsnNode) {
        super(varInsnNode.getOpcode());
        this.varInsnNode = varInsnNode;
        super.var = varInsnNode.var;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(varInsnNode.getNext());
    }

    @Override
    public int getOpcode() {
        return varInsnNode.getOpcode();
    }

    @Override
    public int getType() {
        return varInsnNode.getType();
    }
}
