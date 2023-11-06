package domain;

import java.util.ArrayList;
import java.util.List;

public abstract class MyMethodNode {
    public int access;
    public List<MyAbstractInsnNode> instructions = new ArrayList<>();
    public String name;
    public List<MyLocalVariableNode> localVariables = new ArrayList<>();
    public String desc;
}
