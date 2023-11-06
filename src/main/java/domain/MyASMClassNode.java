package domain;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.ArrayList;
import java.util.List;

public class MyASMClassNode extends MyClassNode {
    private ClassNode classNode;

    public MyASMClassNode(ClassNode classNode) {
        super(classNode.name);
        this.classNode = classNode;
        convertFields();
        super.interfaces = classNode.interfaces;
        super.superName = classNode.superName;
    }

    private void convertFields() {
        List<MyFieldNode> fields = new ArrayList<>();
        for (FieldNode fieldNode : classNode.fields) {
            fields.add(new MyASMFieldNode(fieldNode));
        }
        super.fields = fields;
    }
}
