package domain;

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
        // TODO: abstract getNext?
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        AbstractInsnNode nextInsn = labelNode.getNext();
        if (nextInsn == null) {
            return null;
        }
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(nextInsn);
    }

    @Override
    public MyAbstractInsnNode getPrevious() {
        // TODO: abstract getPrevious?
        MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();
        AbstractInsnNode previousInsn = labelNode.getPrevious();
        if (previousInsn == null) {
            return null;
        }
        return myASMAbstractInsnNodeFactory.constructTypedInsnNode(previousInsn);
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
