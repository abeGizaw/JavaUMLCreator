package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyLocalVariableNode;
import domain.MyMethodNode;
import org.objectweb.asm.tree.AbstractInsnNode;

import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class MyASMMethodNode extends MyMethodNode {
    private final MethodNode methodNode;
    private final MyASMAbstractInsnNodeFactory factory = new MyASMAbstractInsnNodeFactory();

    public MyASMMethodNode(MethodNode methodNode) {
        this.methodNode = methodNode;
        super.access = methodNode.access;
        super.desc = methodNode.desc;
        super.instructions = convertInstructionNodes();
        super.localVariables = methodNode.localVariables == null ? null : convertLocalVariableNodes();
        super.name = methodNode.name;
    }

    private List<MyAbstractInsnNode> convertInstructionNodes() {
        List<MyAbstractInsnNode> instructions = new ArrayList<>();
        for (AbstractInsnNode instruction : methodNode.instructions) {
            instructions.add(factory.constructTypedInsnNode(instruction));
        }
        return instructions;
    }

    private List<MyLocalVariableNode> convertLocalVariableNodes() {
        List<MyLocalVariableNode> localVariables = new ArrayList<>();
        for (LocalVariableNode localVariableNode : methodNode.localVariables) {
            localVariables.add(new MyASMLocalVariableNode(localVariableNode));
        }
        return localVariables;
    }
}
