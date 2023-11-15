package domain.myasm;

import domain.MyLocalVariableNode;
import org.objectweb.asm.tree.LocalVariableNode;

public class MyASMLocalVariableNode extends MyLocalVariableNode {
    private final LocalVariableNode localVariableNode;

    public MyASMLocalVariableNode(LocalVariableNode localVariableNode) {
        this.localVariableNode = localVariableNode;
        super.end = new MyASMLabelNode(localVariableNode.end, new MyASMAbstractInsnNodeFactory());
        super.index = localVariableNode.index;
        super.name = localVariableNode.name;
        super.start = new MyASMLabelNode(localVariableNode.start, new MyASMAbstractInsnNodeFactory());
    }
}
