package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class QDPLKCheck {
    private static final Set<Integer> METHOD_OPCODES = Set.of(Opcodes.H_INVOKEVIRTUAL, Opcodes.H_INVOKESTATIC, Opcodes.H_INVOKESPECIAL, Opcodes.H_INVOKEINTERFACE);
    private static final Set<Integer> LOAD_OPCODES = Set.of(Opcodes.ILOAD, Opcodes.LLOAD, Opcodes.FLOAD, Opcodes.DLOAD, Opcodes.ALOAD);

    Stack<AbstractInsnNode> instructionStack;

    public static void main(String[] args) throws IOException {
        List<String> classNames = new ArrayList<>();
        classNames.add("domain/PLKTestClass");

        QDPLKCheck qdPLKCheck = new QDPLKCheck();
        for (String className : classNames) {
            ClassReader reader = new ClassReader(className);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            qdPLKCheck.run(classNode);
        }
    }

    public QDPLKCheck() {
        instructionStack = new Stack<>();
    }

    private void run(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            checkMethod(methodNode);
        }
    }

    private void checkMethod(MethodNode methodNode) {
        for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
            int insnType = abstractInsnNode.getType();
            if (METHOD_OPCODES.contains(insnType)) {
                if (receiverIsValid(abstractInsnNode)) {
                    // TODO: do something if this is false
                    System.out.printf("receiver for %s is valid\n", methodNode.name);
                } else {
                    System.out.printf("receiver for %s is invalid\n", methodNode.name);
                }
            } else {
                instructionStack.push(abstractInsnNode);
            }
        }
    }

    private boolean receiverIsValid(AbstractInsnNode abstractInsnNode) {
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
        return false;
//        if (LOAD_OPCODES.contains(receiverNode.getOpcode())) {
//            if (receiverNode.getOpcode() == Opcodes.ALOAD) {
//                // TODO: valid load?
//            }
//        }
    }

    private void removeMethodArgument() {
        AbstractInsnNode abstractInsnNode = instructionStack.pop();
        int methodArgumentInsnOpcode = abstractInsnNode.getOpcode();
        if (methodArgumentInsnOpcode == Opcodes.GETFIELD || methodArgumentInsnOpcode == Opcodes.GETSTATIC) {
            instructionStack.pop();
        }
    }
}
