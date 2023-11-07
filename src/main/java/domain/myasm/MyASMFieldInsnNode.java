package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyFieldInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMFieldInsnNode extends MyFieldInsnNode {
    private AbstractInsnNode fieldInsnNode;

    public MyASMFieldInsnNode(AbstractInsnNode abstractInsnNode) {
        super(abstractInsnNode.getOpcode());
        this.fieldInsnNode = abstractInsnNode;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(fieldInsnNode.getNext());
    }

    @Override
    public int getType() {
        return fieldInsnNode.getType();
    }
}
