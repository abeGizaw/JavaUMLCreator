package domain.myasm;

import domain.MyLocalVariableNode;
import org.objectweb.asm.tree.LocalVariableNode;

public class MyASMLocalVariableNode extends MyLocalVariableNode {
    private final LocalVariableNode localVariableNode;
    public MyASMLocalVariableNode(LocalVariableNode localVariableNode){
        this.localVariableNode = localVariableNode;
        super.index = localVariableNode.index;
        super.name = localVariableNode.name;
//        super.end = localVariableNode.end;
//        super.start = localVariableNode.start;
    }
}
