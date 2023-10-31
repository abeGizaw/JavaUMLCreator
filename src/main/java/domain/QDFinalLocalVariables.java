package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
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

        QDFinalLocalVariables qdFinalLocalVariables = new QDFinalLocalVariables();

        qdFinalLocalVariables.checkForFinalLocalVariables(classNode);
    }

    private void checkForFinalLocalVariables(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            System.out.printf("Method: %s\n", methodNode.name);
            checkMethodForFinalLocalVariables(methodNode);
        }
    }

    private void checkMethodForFinalLocalVariables(MethodNode methodNode) {
        List<LocalVariableInfo> localVariables = new ArrayList<>();
        for (LocalVariableNode localVariableNode : methodNode.localVariables) {
            localVariables.add(new LocalVariableInfo(localVariableNode.name, localVariableNode.start.getLabel(), localVariableNode.end.getLabel(), localVariableNode.index));
        }

        for (AbstractInsnNode insn : methodNode.instructions) {
            if (insn.getNext() != null && insn.getNext().getType() == AbstractInsnNode.LABEL) {
                System.out.println("setting in scope");
                // TODO: one error with saying a can be final in testMethod1()
                // Note for not QD: seems like the variable is getting set as in scope after they are initialized, which is also after the value is set, probably aren't technically in scope while they are being initialized
                setInScopeVariables(insn.getNext(), localVariables);
            }
            for (LocalVariableInfo localVariableInfo : localVariables) {
                if (localVariableInfo.isInScope && storeOpcodes.contains(insn.getOpcode())) {
                    System.out.printf("name: %s, index: %d, var: %d\n", localVariableInfo.name, localVariableInfo.index, ((VarInsnNode) insn).var);
                    if (localVariableInfo.index == ((VarInsnNode) insn).var) {
                        handleValueStored(localVariableInfo);
                    }
                }
            }
        }

        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.hasBeenStoredOnce) {
                System.out.printf("%s can be final since the value is not changed.\n", localVariableInfo.name);
            }
        }
    }

    private void setInScopeVariables(AbstractInsnNode insn, List<LocalVariableInfo> localVariables) {
        Label instructionLabel = ((LabelNode) insn).getLabel();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.firstLabel.equals(instructionLabel)) {
                System.out.printf("%s is in scope\n", localVariableInfo.name);
                localVariableInfo.isInScope = true;
            } else if (localVariableInfo.lastLabel.equals(instructionLabel)) {
                localVariableInfo.isInScope = false;
            }
        }
    }

    private void handleValueStored(LocalVariableInfo localVariableInfo) {
        if (localVariableInfo.hasBeenStored) {
            localVariableInfo.hasBeenStoredOnce = false;
        } else {
            localVariableInfo.hasBeenStored = true;
            localVariableInfo.hasBeenStoredOnce = true;
        }
    }

    private class LocalVariableInfo {
        private String name;
        private Label firstLabel;
        private Label lastLabel;
        private int index;
        private boolean isInScope;
        private boolean hasBeenStored;
        private boolean hasBeenStoredOnce;

        private LocalVariableInfo(String name, Label firstLabel, Label lastLabel, int index) {
            this.name = name;
            this.firstLabel = firstLabel;
            this.lastLabel = lastLabel;
            this.index = index;
        }
    }
}
