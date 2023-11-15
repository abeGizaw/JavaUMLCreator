package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyLabel;
import domain.MyLabelNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;

public class MyASMLabelNode extends MyLabelNode {
    private final LabelNode labelNode;
    private final MyASMAbstractInsnNodeFactory factory;

    public MyASMLabelNode(AbstractInsnNode labelNode, MyASMAbstractInsnNodeFactory factory) {
        this.labelNode = (LabelNode) labelNode;
        this.factory = factory;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return factory.constructTypedInsnNode(labelNode.getNext());
    }

    @Override
    public int getOpcode() {
        return labelNode.getOpcode();
    }

    @Override
    public int getType() {
        return labelNode.getType();
    }

    @Override
    public MyLabel getLabel() {
        return new MyASMLabel(labelNode.getLabel());
    }
}
