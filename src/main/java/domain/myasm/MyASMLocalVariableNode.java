package domain.myasm;

import domain.MyLocalVariableNode;
import org.objectweb.asm.tree.LocalVariableNode;

public class MyASMLocalVariableNode extends MyLocalVariableNode {
    private LocalVariableNode localVariableNode;
    public MyASMLocalVariableNode(LocalVariableNode localVariableNode){
        super();
        this.localVariableNode = localVariableNode;
        super.index = localVariableNode.index;
        super.name = localVariableNode.name;
//        super.end = localVariableNode.end;
//        super.start = localVariableNode.start;
    }
}
