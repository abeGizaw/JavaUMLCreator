package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyMethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MyASMMethodInsnNode extends MyMethodInsnNode {
    private final MethodInsnNode methodInsnNode;
    private final MyASMAbstractInsnNodeFactory factory;

    public MyASMMethodInsnNode(AbstractInsnNode methodInsnNode, MyASMAbstractInsnNodeFactory factory) {
        this.methodInsnNode = (MethodInsnNode) methodInsnNode;
        this.factory = factory;
        super.desc = this.methodInsnNode.desc;
        super.name = this.methodInsnNode.name;
        super.owner = this.methodInsnNode.owner;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return factory.constructTypedInsnNode(methodInsnNode.getNext());
    }

    @Override
    public int getOpcode() {
        return methodInsnNode.getOpcode();
    }

    @Override
    public int getType() {
        return methodInsnNode.getType();
    }
}
