package domain;

import domain.myasm.MyASMAbstractInsnNodeFactory;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class MyFieldInsnNodeFactory extends MyASMAbstractInsnNodeFactory {
     public String name;
     public String desc;

     public MyFieldInsnNodeFactory(AbstractInsnNode a) {
          super(a);
     }
}
