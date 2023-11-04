package domain.ariscode;

import org.objectweb.asm.tree.MethodNode;

public class MyASMMethodNode extends MyMethodNode {
    private MethodNode methodNode;
    public MyASMMethodNode(MethodNode method) {
        super();
        this.methodNode = method;
        super.access = method.access;
        super.name = method.name;
        super.desc = method.desc;
        //convert instructions
        //convert localVariables

    }
}
