package domain.myasm;

import domain.MyClassNode;
import domain.MyFieldNode;
import domain.MyInnerClassNode;
import domain.MyMethodNode;
import org.objectweb.asm.tree.*;

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
        super.innerClasses = convertInnerClasses();
        super.annotations = convertAnnotations();
    }

    private List<MyASMAnnotationNode> convertAnnotations() {
        List<MyASMAnnotationNode> annotations = new ArrayList<>();
        if (classNode.visibleAnnotations != null) {
            for (AnnotationNode visibleAnnot : classNode.visibleAnnotations) {
                annotations.add(new MyASMAnnotationNode(visibleAnnot));
            }
        }
        if (classNode.invisibleAnnotations != null) {
            for (AnnotationNode invisibleAnnot : classNode.invisibleAnnotations) {
                annotations.add(new MyASMAnnotationNode(invisibleAnnot));
            }
        }
        return annotations;
    }

    private List<MyInnerClassNode> convertInnerClasses() {
        List<MyInnerClassNode> innerClassNodes = new ArrayList<>();
        for(InnerClassNode icn : classNode.innerClasses){
            innerClassNodes.add(new MyASMInnerClassNode(icn));
        }
        return innerClassNodes;
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
