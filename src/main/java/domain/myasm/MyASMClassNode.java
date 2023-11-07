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

    private List<MyMethodNode> convertMethods() {
        List<MyMethodNode> myMethods  = new ArrayList<>();
        for(MethodNode method : classNode.methods){
            myMethods.add(new MyASMMethodNode(method));
        }
        return myMethods;
    }

    private List<MyFieldNode> convertFields() {
        List<MyFieldNode> myFields = new ArrayList<>();
        for (FieldNode fieldNode: classNode.fields){
            myFields.add(new MyASMFieldNode(fieldNode));
        }
        return myFields;
    }
}
