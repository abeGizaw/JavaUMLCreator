package domain.ariscode;

import org.objectweb.asm.tree.FieldInsnNode;

public class MyASMFieldInsnNode extends MyFieldInsnNode {
    private final FieldInsnNode fieldNode;

    MyASMFieldInsnNode(FieldInsnNode f) {
        fieldNode = f;
        super.name = fieldNode.name;
        super.desc = fieldNode.desc;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return new MyASMAbstractInsnNode(fieldNode.getNext());
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
