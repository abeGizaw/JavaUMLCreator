package domain.ariscode;

import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class MyFieldInsnNode extends MyASMAbstractInsnNode {
     public String name;
     public String desc;

     MyFieldInsnNode(AbstractInsnNode a) {
          super(a);
     }
}
