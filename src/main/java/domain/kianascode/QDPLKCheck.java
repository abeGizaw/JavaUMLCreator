package domain.kianascode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;

public class QDPLKCheck {
    private static final Set<Integer> METHOD_OPCODES = Set.of(Opcodes.H_INVOKEVIRTUAL, Opcodes.H_INVOKESTATIC, Opcodes.H_INVOKESPECIAL, Opcodes.H_INVOKEINTERFACE);

    Stack<AbstractInsnNode> instructionStack;

    public QDPLKCheck() {
        instructionStack = new Stack<>();
    }

    public void run(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            checkMethod(methodNode);
        }
    }

    private void checkMethod(MethodNode methodNode) {
        LocalVariableManager localVariableManager = new LocalVariableManager(methodNode);
        for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
            localVariableManager.updateVariableScopes(abstractInsnNode);
            int insnType = abstractInsnNode.getType();
            if (METHOD_OPCODES.contains(insnType)) {
                if (isConstructor(abstractInsnNode)) {
                    localVariableManager.addCreatedVariable(abstractInsnNode.getNext());
                } else {
                    if (receiverIsValid(abstractInsnNode, localVariableManager)) {
                        System.out.printf("receiver for %s is valid\n", ((MethodInsnNode) abstractInsnNode).name);
                    } else {
                        System.out.printf("receiver for %s is invalid\n", ((MethodInsnNode) abstractInsnNode).name);
                    }
                }
            } else {
                instructionStack.push(abstractInsnNode);
            }
        }
    }

    private boolean isConstructor(AbstractInsnNode abstractInsnNode) {
        if (abstractInsnNode.getOpcode() != Opcodes.INVOKESPECIAL)  {
            return false;
        }
        return (((MethodInsnNode) abstractInsnNode).name).equals("<init>");
    }

    private boolean receiverIsValid(AbstractInsnNode abstractInsnNode, LocalVariableManager localVariableManager) {
        // remove arguments
        Type methodType = Type.getType(((MethodInsnNode) abstractInsnNode).desc);
        int numArguments = methodType.getArgumentTypes().length;
        for (int i = 0; i < numArguments; i++) {
            removeMethodArgument();
        }

        AbstractInsnNode receiverNode = instructionStack.pop();

        // is field?
        if (receiverNode.getOpcode() == Opcodes.GETFIELD || receiverNode.getOpcode() == Opcodes.GETSTATIC) {
            AbstractInsnNode fieldsClassLoadInsn = instructionStack.pop();
            return (fieldsClassLoadInsn.getOpcode() == Opcodes.ALOAD) && (((VarInsnNode) fieldsClassLoadInsn).var == 0);
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
        return ((VarInsnNode) receiverNode).var == 0;
    }

    private void removeMethodArgument() {
        AbstractInsnNode abstractInsnNode = instructionStack.pop();
        int methodArgumentInsnOpcode = abstractInsnNode.getOpcode();
        if (methodArgumentInsnOpcode == Opcodes.GETFIELD || methodArgumentInsnOpcode == Opcodes.GETSTATIC) {
            instructionStack.pop();
        }
    }
}
