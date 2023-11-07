package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyVarInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MyASMVarInsnNode extends MyVarInsnNode {
    private AbstractInsnNode varInsnNode;

    public MyASMVarInsnNode(AbstractInsnNode varInsnNode) {
        super(varInsnNode.getOpcode());
        this.varInsnNode = varInsnNode;
        super.var = ((VarInsnNode) varInsnNode).var;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        AbstractInsnNode nextInsn = varInsnNode.getNext();
        if (nextInsn == null) {
            return null;
        }
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(varInsnNode.getNext());
    }

    @Override
    public MyAbstractInsnNode getPrevious() {
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        AbstractInsnNode previousInsn = varInsnNode.getPrevious();
        if (previousInsn == null) {
            return null;
        }
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(varInsnNode.getPrevious());
    }

    @Override
    public int getType() {
        return varInsnNode.getType();
    }
}
