package domain;

import java.util.List;

public abstract class MyClassNode {
    public List<MyFieldNode> fields;
    public List<String> interfaces;
    public List<MyMethodNode> methods;
    public String name;
    public String superName;
}
