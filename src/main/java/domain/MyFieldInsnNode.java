package domain;

import domain.myasm.MyASMAbstractInsnNodeFactory;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class MyFieldInsnNode extends MyAbstractInsnNode {
     public String name;
     public String desc;
}
