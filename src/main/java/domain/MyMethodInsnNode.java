package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class MyMethodInsnNode extends MyAbstractInsnNode {
    public String desc;
    public String name;

    public MyMethodInsnNode(int opcode) {
        super(opcode);
    }
}
