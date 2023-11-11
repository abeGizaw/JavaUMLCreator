package domain.myasm;

import domain.MyClassNode;
import domain.MyFieldNode;
import domain.MyMethodNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class MyASMClassNode extends MyClassNode {
    private final ClassNode classNode;

    public MyASMClassNode(ClassNode classNode) {
        this.classNode = classNode;
        super.access = classNode.access;
        super.name = classNode.name;
        super.superName = classNode.superName;
        super.interfaces = classNode.interfaces;
        super.fields = convertFields();
        super.methods = convertMethods();
    }

    private List<MyFieldNode> convertFields() {
        List<MyFieldNode> fields = new ArrayList<>();
        for (FieldNode fieldNode : classNode.fields) {
            fields.add(new MyASMFieldNode(fieldNode));
        }
        return fields;
    }

    private List<MyMethodNode> convertMethods() {
        List<MyMethodNode> methods = new ArrayList<>();
        for (MethodNode methodNode : classNode.methods) {
            methods.add(new MyASMMethodNode(methodNode));
        }
        return methods;
    }

    /**
     * NOTE: This getter is only used for transformations.
     * This is done so that I do not need to create adapters for all visitors.
     * Transformations will rely on ASM classNodes.
     *
     * @return
     */
    public ClassNode getClassNode() {
        return classNode;
    }
}
