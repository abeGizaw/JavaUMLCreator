package domain;

import java.util.ArrayList;
import java.util.List;

public abstract class MyClassNode {
    public int access;
    public List<MyFieldNode> fields = new ArrayList<>();
    public List<String> interfaces = new ArrayList<>();
    public List<MyMethodNode> methods = new ArrayList<>();
    public String name;
    public String superName;
}
