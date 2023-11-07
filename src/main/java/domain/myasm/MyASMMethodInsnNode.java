package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyMethodInsnNode;
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
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(methodInsnNode.getNext());
    }

    @Override
    public int getType() {
        return methodInsnNode.getType();
    }
}
