package domain;

import java.util.List;

public abstract class MyClassNode {
    public int access;
    public List<MyFieldNode> fields;
    public List<String> interfaces;
    public List<MyMethodNode> methods;
    public String name;
    public String superName;
    public List<MyInnerClassNode> innerClasses;

}
