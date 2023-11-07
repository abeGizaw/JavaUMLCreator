package domain;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MyASMMethodInsnNode extends MyMethodInsnNode {
    private AbstractInsnNode methodInsnNode;

    public MyASMMethodInsnNode(AbstractInsnNode methodInsnNode) {
        super(methodInsnNode.getOpcode());
        this.methodInsnNode = methodInsnNode;
        super.desc = ((MethodInsnNode) methodInsnNode).desc;
        super.name = ((MethodInsnNode) methodInsnNode).name;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        AbstractInsnNode nextInsn = methodInsnNode.getNext();
        if (nextInsn == null) {
            return null;
        }
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(methodInsnNode.getNext());
    }

    @Override
    public MyAbstractInsnNode getPrevious() {
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        AbstractInsnNode previousInsn = methodInsnNode.getPrevious();
        if (previousInsn == null) {
            return null;
        }
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(methodInsnNode.getPrevious());
    }

    @Override
    public int getType() {
        return methodInsnNode.getType();
    }
}
