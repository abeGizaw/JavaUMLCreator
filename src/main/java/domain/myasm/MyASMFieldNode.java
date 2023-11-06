package domain.myasm;

import org.objectweb.asm.tree.FieldNode;

public class MyASMFieldNode extends MyFieldNode {

    private FieldNode fieldNode;
    public MyASMFieldNode(FieldNode fieldNode) {
        super();
        this.fieldNode = fieldNode;
        super.desc = fieldNode.desc;
        super.name = fieldNode.name;
        super.access = fieldNode.access;
    }
}
