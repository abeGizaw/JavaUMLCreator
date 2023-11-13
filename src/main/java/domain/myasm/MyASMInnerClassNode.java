package domain.myasm;

import domain.MyInnerClassNode;
import org.objectweb.asm.tree.InnerClassNode;

public class MyASMInnerClassNode extends MyInnerClassNode {
    public final InnerClassNode innerClassNode;
    public MyASMInnerClassNode(InnerClassNode icn){
        this.innerClassNode = icn;
        super.access = icn.access;
        super.name = icn.name;
    }
}
