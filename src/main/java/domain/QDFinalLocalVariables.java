package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.*;

public class QDFinalLocalVariables {
    private static final Set<Integer> storeOpcodes = Set.of(Opcodes.ISTORE, Opcodes.LSTORE, Opcodes.FSTORE, Opcodes.DSTORE, Opcodes.ASTORE);

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a class name: ");
//        String className = scanner.next();
        String className = "domain/FinalLocalVariablesTestClass";

        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        checkForFinalLocalVariables(classNode);
    }

    private static void checkForFinalLocalVariables(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            System.out.printf("Method: %s\n", methodNode.name);
            checkMethodForFinalLocalVariables(methodNode);
        }
    }

    private static void checkMethodForFinalLocalVariables(MethodNode methodNode) {
        Set<Integer> storedOperands = new HashSet<>();
        Set<Integer> storedOnceOperands = new HashSet();
        for (AbstractInsnNode insn : methodNode.instructions) {
            if (storeOpcodes.contains(insn.getOpcode())) {
                int operand = ((VarInsnNode) insn).var;
                if (storedOnceOperands.contains(operand)) {
                    storedOnceOperands.remove(operand);
                } else {
                    storedOperands.add(operand);
                    storedOnceOperands.add(operand);
                }
            }
        }

        for (LocalVariableNode localVariableNode : methodNode.localVariables) {
            if (storedOnceOperands.contains(localVariableNode.index)) {
                System.out.printf("%s can be final since the value is not changed.\n", localVariableNode.name);
            }
        }

        // TODO: doesn't work with different variable scopes, like variables in if statements since the locations get reused after the if statement ends (maybe something recursive or a stack?)
    }
}
