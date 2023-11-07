package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyMethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.lang.reflect.Method;

public class MyASMMethodInsnNode extends MyMethodInsnNode {
    private final MethodInsnNode methodInsnNode;
    private final MyASMAbstractInsnNodeFactory factory = new MyASMAbstractInsnNodeFactory();

    public MyASMMethodInsnNode(AbstractInsnNode methodInsnNode) {
        this.methodInsnNode = (MethodInsnNode) methodInsnNode;
        super.desc = this.methodInsnNode.desc;
        super.name = this.methodInsnNode.name;
    }

    @Override
    public MyAbstractInsnNode getNext() {
        return factory.constructTypedInsnNode(methodInsnNode.getNext());
    }

    @Override
    public int getOpcode() {
        return methodInsnNode.getOpcode();
    }

    @Override
    public int getType() {
        return methodInsnNode.getType();
    }
}
