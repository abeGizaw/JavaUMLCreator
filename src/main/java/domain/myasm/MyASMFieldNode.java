package domain.myasm;

import domain.MyFieldNode;
import org.objectweb.asm.tree.FieldNode;

public class MyASMFieldNode extends MyFieldNode {

    private final FieldNode fieldNode;
    public MyASMFieldNode(FieldNode fieldNode) {
        this.fieldNode = fieldNode;
        super.desc = fieldNode.desc;
        super.name = fieldNode.name;
        super.access = fieldNode.access;
    }
}
