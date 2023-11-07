package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyLocalVariableNode;
import domain.MyMethodNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyASMMethodNode extends MyMethodNode {
    private final MethodNode methodNode;
    private final MyASMAbstractInsnNodeFactory factory = new MyASMAbstractInsnNodeFactory();
    public MyASMMethodNode(MethodNode method) {
        this.methodNode = method;
        super.access = method.access;
        super.name = method.name;
        super.desc = method.desc;
        super.instructions = convertInstructions(method.instructions);
        super.localVariables = method.localVariables == null ? null : convertLocalVariables(method.localVariables);

    }

    private List<MyLocalVariableNode> convertLocalVariables(List<LocalVariableNode> localVariables) {
        List<MyLocalVariableNode> myLocalVariableNodes = new ArrayList<>();
        for(LocalVariableNode localVariableNode: localVariables){
            myLocalVariableNodes.add(new MyASMLocalVariableNode(localVariableNode));
        }
        return myLocalVariableNodes;
    }

    private List<MyAbstractInsnNode> convertInstructions(InsnList instructions) {
        List<MyAbstractInsnNode> newInstructions = new ArrayList<>();
        for(AbstractInsnNode instruction: instructions){
            newInstructions.add(factory.constructTypedInsnNode(instruction));
        }
        return newInstructions;
    }
}
