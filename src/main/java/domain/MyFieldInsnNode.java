package domain;

import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class MyFieldInsnNode extends MyAbstractInsnNode {
    public String name;
    public String desc;

    public MyFieldInsnNode(AbstractInsnNode a) {
    }
}
