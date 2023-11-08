package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyVarInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MyASMVarInsnNode extends MyVarInsnNode {
    private final VarInsnNode varInsnNode;
    private final MyASMAbstractInsnNodeFactory factory = new MyASMAbstractInsnNodeFactory();

    public MyASMVarInsnNode(AbstractInsnNode varInsnNode) {
        this.varInsnNode = (VarInsnNode) varInsnNode;
        super.var = this.varInsnNode.var;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return factory.constructTypedInsnNode(varInsnNode.getNext());
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
