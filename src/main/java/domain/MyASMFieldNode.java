package domain;

import org.objectweb.asm.tree.FieldNode;

public class MyASMFieldNode extends MyFieldNode {
    private FieldNode fieldNode;

    public MyASMFieldNode(FieldNode fieldNode) {
        this.fieldNode = fieldNode;
        super.desc = fieldNode.desc;
    }
}
