package domain.kianascode;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;

public class QDFinalLocalVariables {
    private static final Set<Integer> STORE_OPCODES = Set.of(Opcodes.ISTORE, Opcodes.LSTORE, Opcodes.FSTORE, Opcodes.DSTORE, Opcodes.ASTORE);

    private List<LocalVariableInfo> localVariables;
    private boolean prevInsnIsLabel;
    private AbstractInsnNode prevInsn;

    public QDFinalLocalVariables() {
        localVariables = new ArrayList<>();
        prevInsnIsLabel = false;
        prevInsn = null;
    }

    public void run(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            System.out.printf("Method: %s\n", methodNode.name);
            localVariables.clear();
            addLocalVariables(methodNode);
            checkMethodForFinalLocalVariables(methodNode);
            printResults();
        }
    }

    private void checkMethodForFinalLocalVariables(MethodNode methodNode) {
        for (AbstractInsnNode insn : methodNode.instructions) {
            updateVariableScopes(insn);
            for (LocalVariableInfo localVariableInfo : localVariables) {
                if (localVariableInfo.isInScope && STORE_OPCODES.contains(insn.getOpcode())) {
                    if (localVariableInfo.index == ((VarInsnNode) insn).var) {
                        handleValueStored(localVariableInfo);
                        break;
                    }
                }
            }
        }
    }

    private void addLocalVariables(MethodNode methodNode) {
        for (LocalVariableNode localVariableNode : methodNode.localVariables) {
            localVariables.add(new LocalVariableInfo(localVariableNode.name, localVariableNode.start.getLabel(), localVariableNode.end.getLabel(), localVariableNode.index));
        }
    }

    private void updateVariableScopes(AbstractInsnNode insn) {
        if (insn.getNext() != null && insn.getNext().getType() == AbstractInsnNode.LABEL) {
            setInScopeVariables(insn.getNext(), localVariables);
        }

        if (prevInsnIsLabel) {
            setOutOfScopeVariables(prevInsn, localVariables);
            prevInsnIsLabel = false;
        }

        if (insn.getType() == AbstractInsnNode.LABEL) {
            prevInsnIsLabel = true;
            prevInsn = insn;
        }
    }

    private void setInScopeVariables(AbstractInsnNode insn, List<LocalVariableInfo> localVariables) {
        Label instructionLabel = ((LabelNode) insn).getLabel();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.firstLabel.equals(instructionLabel)) {
                localVariableInfo.isInScope = true;
            }
        }
    }

    private void setOutOfScopeVariables(AbstractInsnNode insn, List<LocalVariableInfo> localVariables) {
        Label instructionLabel = ((LabelNode) insn).getLabel();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.lastLabel.equals(instructionLabel)) {
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

    private void printResults() {
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.hasBeenStoredOnce) {
                System.out.printf("%s can be final since the value is not changed.\n", localVariableInfo.name);
            }
        }
    }

    private class LocalVariableInfo {
        private final String name;
        private final Label firstLabel;
        private final Label lastLabel;
        private final int index;
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