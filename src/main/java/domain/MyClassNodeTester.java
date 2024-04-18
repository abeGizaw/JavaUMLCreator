package domain;

import domain.myasm.MyASMAnnotationNode;

import java.util.ArrayList;
import java.util.List;

public class MyClassNodeTester extends MyClassNode{
    public MyClassNodeTester(MyClassNode myClassNode) {
        super.access = myClassNode.access;
        super.fields = new ArrayList<>(myClassNode.fields);
        super.interfaces = new ArrayList<>(myClassNode.interfaces);
        super.methods = new ArrayList<>(myClassNode.methods);
        super.name = myClassNode.name;
        super.superName = myClassNode.superName;
        super.innerClasses = new ArrayList<>(myClassNode.innerClasses);
        super.annotations = new ArrayList<>(myClassNode.annotations);
    }

    public MyClassNodeTester() {
        super();
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void setFields(List<MyFieldNode> fields) {
        this.fields = new ArrayList<>(fields);
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = new ArrayList<>(interfaces);
    }

    public void setMethods(List<MyMethodNode> methods) {
        this.methods = new ArrayList<>(methods);
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public void setInnerClasses(List<MyInnerClassNode> innerClasses) {
        this.innerClasses = new ArrayList<>(innerClasses);
    }

    public void setAnnotations(List<MyASMAnnotationNode> annotations) {
        this.annotations = new ArrayList<>(annotations);
    }



}
