package domain.myasm;

import domain.MyAbstractInsnNode;
import domain.MyMethodNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class MyASMMethodNode extends MyMethodNode {
    private final MethodNode methodNode;
    private final MyASMAbstractInsnNodeFactory factory = new MyASMAbstractInsnNodeFactory();
    public MyASMMethodNode(MethodNode method) {
        super();
        this.methodNode = method;
        super.access = method.access;
        super.name = method.name;
        super.desc = method.desc;
        super.instructions = convertInstructions(method.instructions);
        //convert localVariables

    }

    private List<MyAbstractInsnNode> convertInstructions(InsnList instructions) {
        List<MyAbstractInsnNode> newInstructions = new ArrayList<>();
        for(AbstractInsnNode instruction: instructions){
            newInstructions.add(factory.constructTypedInsnNode(instruction));
        }
        return newInstructions;
    }
}
