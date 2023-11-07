package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyFieldInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class MyASMFieldInsnNode extends MyFieldInsnNode {
    private final FieldInsnNode fieldInsnNode;
    private final MyASMAbstractInsnNodeFactory factory = new MyASMAbstractInsnNodeFactory();

    public MyASMFieldInsnNode(AbstractInsnNode abstractInsnNode) {
        this.fieldInsnNode = (FieldInsnNode) abstractInsnNode;
        super.name = this.fieldInsnNode.name;
        super.desc = this.fieldInsnNode.desc;
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
