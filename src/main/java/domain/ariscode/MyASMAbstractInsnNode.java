package domain.ariscode;

import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMAbstractInsnNode extends MyAbstractInsnNode{
    private final AbstractInsnNode abstractInsnNode;

    MyASMAbstractInsnNode(AbstractInsnNode a){
        abstractInsnNode = a;

    }

    @Override
    public MyAbstractInsnNode getNext() {
        return  new MyASMAbstractInsnNode(abstractInsnNode.getNext());
    }

    @Override
    public int getOpcode() {
        return abstractInsnNode.getOpcode();
    }

    @Override
    public int getType() {
        return abstractInsnNode.getType();
    }
}
