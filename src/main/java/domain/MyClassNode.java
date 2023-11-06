package domain;

import java.util.List;

public abstract class MyClassNode {
    public List<MyFieldNode> fields;
    public List<String> interfaces;
    public String name;
    public String superName;

    public MyClassNode(String name) {
        this.name = name;
    }
}
