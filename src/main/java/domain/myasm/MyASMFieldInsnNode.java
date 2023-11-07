package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyFieldInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class MyASMFieldInsnNode extends MyFieldInsnNode {
    private final FieldInsnNode fieldNode;

    public MyASMFieldInsnNode(AbstractInsnNode node) {
        fieldNode = (FieldInsnNode) node;
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
