package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyFieldInsnNodeFactory;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class MyASMFieldInsnNodeFactory extends MyFieldInsnNodeFactory {
    private final FieldInsnNode fieldNode;

    MyASMFieldInsnNodeFactory(AbstractInsnNode node) {
        super(node);
        fieldNode = (FieldInsnNode) node;
        super.name = fieldNode.name;
        super.desc = fieldNode.desc;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return new MyASMAbstractInsnNodeFactory(fieldNode.getNext());
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
