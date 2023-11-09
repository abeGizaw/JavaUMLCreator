package domain.checks;

import domain.*;
import domain.myasm.MyASMType;

import java.util.*;

public class PrincipleOfLeastKnowledge implements Check {
    private static final Set<Integer> METHOD_OPCODES = Set.of(MyOpcodes.H_INVOKEVIRTUAL, MyOpcodes.H_INVOKESTATIC, MyOpcodes.H_INVOKESPECIAL, MyOpcodes.H_INVOKEINTERFACE);

    Stack<MyAbstractInsnNode> instructionStack;

    public PrincipleOfLeastKnowledge() {
        instructionStack = new Stack<>();
    }

    public List<Message> run(MyClassNode classNode) {
        for (MyMethodNode myMethodNode : classNode.methods) {
            checkMethod(myMethodNode);
        }
        return null;
    }

    private void checkMethod(MyMethodNode myMethodNode) {
        LocalVariableManager localVariableManager = new LocalVariableManager(myMethodNode);
        for (MyAbstractInsnNode myAbstractInsnNode : myMethodNode.instructions) {
            localVariableManager.updateVariableScopes(myAbstractInsnNode);
            int insnType = myAbstractInsnNode.getType();
            if (METHOD_OPCODES.contains(insnType)) {
                if (isConstructor(myAbstractInsnNode)) {
                    localVariableManager.addCreatedVariable(myAbstractInsnNode.getNext());
                } else {
                    if (receiverIsValid(myAbstractInsnNode, localVariableManager)) {
                        System.out.printf("receiver for %s is valid\n", ((MyMethodInsnNode) myAbstractInsnNode).name);
                    } else {
                        System.out.printf("receiver for %s is invalid\n", ((MyMethodInsnNode) myAbstractInsnNode).name);
                    }
                }
            } else {
                instructionStack.push(myAbstractInsnNode);
            }
        }
    }

    private boolean isConstructor(MyAbstractInsnNode myAbstractInsnNode) {
        if (myAbstractInsnNode.getOpcode() != MyOpcodes.INVOKESPECIAL)  {
            return false;
        }
        return (((MyMethodInsnNode) myAbstractInsnNode).name).equals("<init>");
    }

    private boolean receiverIsValid(MyAbstractInsnNode abstractInsnNode, LocalVariableManager localVariableManager) {
        // remove arguments
        MyType myType = new MyASMType();
        MyType methodType = myType.getType(((MyMethodInsnNode) abstractInsnNode).desc);
        int numArguments = methodType.getArgumentTypes().length;
        for (int i = 0; i < numArguments; i++) {
            removeMethodArgument();
        }

        MyAbstractInsnNode receiverNode = instructionStack.pop();

        // is field?
        if (receiverNode.getOpcode() == MyOpcodes.GETFIELD || receiverNode.getOpcode() == MyOpcodes.GETSTATIC) {
            MyAbstractInsnNode fieldsClassLoadInsn = instructionStack.pop();
            return (fieldsClassLoadInsn.getOpcode() == MyOpcodes.ALOAD) && (((MyVarInsnNode) fieldsClassLoadInsn).var == 0);
        }

        // is created?
        if (localVariableManager.isCreatedVariable(receiverNode)) {
            return true;
        }

        // is parameter?
        if (localVariableManager.isParameter(receiverNode)) {
                return true;
        }

        // is this?
        if (receiverNode.getType() != MyAbstractInsnNode.VAR_INSN) {
            return false;
        }
        return ((MyVarInsnNode) receiverNode).var == 0;
    }

    private void removeMethodArgument() {
        MyAbstractInsnNode abstractInsnNode = instructionStack.pop();
        int methodArgumentInsnOpcode = abstractInsnNode.getOpcode();
        if (methodArgumentInsnOpcode == MyOpcodes.GETFIELD || methodArgumentInsnOpcode == MyOpcodes.GETSTATIC) {
            instructionStack.pop();
        }
    }
}
