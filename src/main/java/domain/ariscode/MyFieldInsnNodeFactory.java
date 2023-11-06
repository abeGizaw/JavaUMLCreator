package domain.ariscode;

import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class MyFieldInsnNodeFactory extends MyASMAbstractInsnNodeFactory {
     public String name;
     public String desc;

     MyFieldInsnNodeFactory(AbstractInsnNode a) {
          super(a);
     }
}
