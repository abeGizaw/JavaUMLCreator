package domain;

import java.util.List;

public abstract class MyMethodNode {
    public int access;
    public String desc;
    public List<MyAbstractInsnNode> instructions;
    public List<MyLocalVariableNode> localVariables;
    public String name;
}
