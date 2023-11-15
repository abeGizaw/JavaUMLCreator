package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyFieldInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class MyASMFieldInsnNode extends MyFieldInsnNode {
    private final FieldInsnNode fieldInsnNode;
    private final MyASMAbstractInsnNodeFactory factory;

    public MyASMFieldInsnNode(AbstractInsnNode abstractInsnNode, MyASMAbstractInsnNodeFactory factory) {
        this.fieldInsnNode = (FieldInsnNode) abstractInsnNode;
        super.name = this.fieldInsnNode.name;
        super.desc = this.fieldInsnNode.desc;
        super.owner = this.fieldInsnNode.owner;
        this.factory = factory;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return factory.constructTypedInsnNode(fieldInsnNode.getNext());
    }

    @Override
    public int getOpcode() {
        return fieldInsnNode.getOpcode();
    }

    @Override
    public int getType() {
        return fieldInsnNode.getType();
    }
}
