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
    private ClassNode classNode;

    public MyASMClassNode(ClassNode classNode) {
        this.classNode = classNode;
        super.fields = convertFields();
        super.interfaces = classNode.interfaces;
        super.name = classNode.name;
        super.methods = convertMethods();
        super.superName = classNode.superName;
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
}
