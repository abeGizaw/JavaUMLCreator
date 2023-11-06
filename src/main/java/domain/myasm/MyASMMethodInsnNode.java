package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyMethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MyASMMethodInsnNode extends MyMethodInsnNode {
    private final MethodInsnNode fieldNode;

    public MyASMMethodInsnNode(AbstractInsnNode node) {
        super(node);
        fieldNode = (MethodInsnNode) node;
        super.name = fieldNode.name;
        super.desc = fieldNode.desc;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return new MyASMFieldInsnNode(fieldNode.getNext());
    }

    @Override
    public int getOpcode() {
        return fieldNode.getOpcode();
    }

    @Override
    public int getType() {
        return fieldNode.getType();
    }
}
