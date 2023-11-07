package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyLabel;
import domain.MyLabelNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class MyASMLabelNode extends MyLabelNode {
    private AbstractInsnNode labelNode;

    public MyASMLabelNode(AbstractInsnNode labelNode) {
        super(labelNode.getOpcode());
        this.labelNode = labelNode;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(labelNode.getNext());
    }

    @Override
    public int getType() {
        return labelNode.getType();
    }

    @Override
    public MyLabel getLabel() {
        return new MyASMLabel(((LabelNode) labelNode).getLabel());
    }
}
