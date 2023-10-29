package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.*;

public class QDFinalLocalVariables {
    private static final Set<Integer> storeOpcodes = Set.of(Opcodes.ISTORE, Opcodes.LSTORE, Opcodes.FSTORE, Opcodes.DSTORE, Opcodes.ASTORE);
    private static final Set<Integer> loadOpcodes = Set.of(Opcodes.ILOAD, Opcodes.LLOAD, Opcodes.FLOAD, Opcodes.DLOAD, Opcodes.ALOAD);

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
            checkMethodForFinalLocalVariables(methodNode);
        }
    }

    private static void checkMethodForFinalLocalVariables(MethodNode methodNode) {
        Set<String> notFinalLocalVariables = new HashSet();
        for (AbstractInsnNode insn : methodNode.instructions) {
            if (storeOpcodes.contains(insn.getOpcode())) {
                System.out.println("local variable");
            }
        }
    }
}
