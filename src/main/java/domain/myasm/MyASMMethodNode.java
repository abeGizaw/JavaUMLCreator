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
    private MethodNode methodNode;
    private MyASMAbstractInsnNodeFactory myASMAbstractInsnNodeFactory;

    public MyASMMethodNode(MethodNode methodNode) {
        this.methodNode = methodNode;
        myASMAbstractInsnNodeFactory = new MyASMAbstractInsnNodeFactory();

        super.instructions = convertInstructionNodes();
        super.localVariables = convertLocalVariableNodes();
        super.name = methodNode.name;
    }

    private List<MyAbstractInsnNode> convertInstructionNodes() {
        List<MyAbstractInsnNode> instructions = new ArrayList<>();
        for (AbstractInsnNode instruction : methodNode.instructions) {
            MyAbstractInsnNode newInstructionNode = myASMAbstractInsnNodeFactory.constructTypedInsnNode(instruction);
            instructions.add(newInstructionNode);
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
