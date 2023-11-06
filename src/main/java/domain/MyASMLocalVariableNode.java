package domain;

import org.objectweb.asm.tree.LocalVariableNode;

public class MyASMLocalVariableNode extends MyLocalVariableNode{
    private LocalVariableNode localVariableNode;

    public MyASMLocalVariableNode(LocalVariableNode localVariableNode) {
        this.localVariableNode = localVariableNode;
        super.end = new MyASMLabelNode(localVariableNode.end);
        super.index = localVariableNode.index;
        super.name = localVariableNode.name;
        super.start = new MyASMLabelNode(localVariableNode.start);
    }
}
