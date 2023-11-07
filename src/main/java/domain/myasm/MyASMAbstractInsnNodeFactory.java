package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyDefaultInsnNode;
import domain.MyOpcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

public class MyASMAbstractInsnNodeFactory {

    public MyAbstractInsnNode constructTypedInsnNode(AbstractInsnNode node) {
        int opcode = node.getOpcode();
        if (opcode == MyOpcodes.PUTFIELD || opcode == MyOpcodes.PUTSTATIC) {
            return new MyASMFieldInsnNode(node);
        }

        return new MyDefaultInsnNode(node);

    }


}
