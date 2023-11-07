package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyMethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MyASMMethodInsnNode extends MyMethodInsnNode {
    private final MethodInsnNode methodInsnNode;

    public MyASMMethodInsnNode(AbstractInsnNode node) {
        methodInsnNode = (MethodInsnNode) node;
        super.name = methodInsnNode.name;
        super.desc = methodInsnNode.desc;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return new MyASMFieldInsnNode(methodInsnNode.getNext());
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
