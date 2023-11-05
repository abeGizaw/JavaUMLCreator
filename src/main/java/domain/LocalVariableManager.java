package domain;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.Set;

public class LocalVariableManager {
    private static final Set<Integer> STORE_OPCODES = Set.of(Opcodes.ISTORE, Opcodes.LSTORE, Opcodes.FSTORE, Opcodes.DSTORE, Opcodes.ASTORE);
    private static final Set<Integer> LOAD_OPCODES = Set.of(Opcodes.ILOAD, Opcodes.LLOAD, Opcodes.FLOAD, Opcodes.DLOAD, Opcodes.ALOAD);

    private Set<LocalVariableInfo> localVariables;
    private Set<LocalVariableInfo> createdVariables;

    public LocalVariableManager(MethodNode methodNode) {
        localVariables = new HashSet<>();
        for (LocalVariableNode localVariableNode : methodNode.localVariables) {
            localVariables.add(new LocalVariableInfo(localVariableNode.name, localVariableNode.start.getLabel(), localVariableNode.end.getLabel(), localVariableNode.index));
        }

        createdVariables = new HashSet<>();
    }

    public void updateVariableScopes(AbstractInsnNode insn) {
        if (insn.getNext() != null && insn.getNext().getType() == AbstractInsnNode.LABEL) {
            setInScopeVariables(insn.getNext(), localVariables);
        }

        AbstractInsnNode prevInsn = insn.getPrevious();
        if (prevInsn != null && prevInsn.getOpcode() == AbstractInsnNode.LABEL) {
            setOutOfScopeVariables(prevInsn, localVariables);
        }
    }

    private void setInScopeVariables(AbstractInsnNode insn, Set<LocalVariableInfo> localVariables) {
        Label instructionLabel = ((LabelNode) insn).getLabel();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getFirstLabel().equals(instructionLabel)) {
                localVariableInfo.setIsInScope(true);
            }
        }
    }

    private void setOutOfScopeVariables(AbstractInsnNode insn, Set<LocalVariableInfo> localVariables) {
        Label instructionLabel = ((LabelNode) insn).getLabel();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getLastLabel().equals(instructionLabel)) {
                localVariableInfo.setIsInScope(false);
            }
        }
    }

    public void addCreatedVariable(AbstractInsnNode abstractInsnNode) {
        if (!STORE_OPCODES.contains(abstractInsnNode.getOpcode())) {
            return;
        }
        int index = ((VarInsnNode) abstractInsnNode).var;
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getIndex() == index && !localVariableInfo.getIsInScope()) { // !localVariableInfo.getIsInScope because the variable shouldn't have been created yet
                createdVariables.add(localVariableInfo);
            }
        }
    }

    public boolean isCreatedVariable(AbstractInsnNode abstractInsnNode) {
        if (!LOAD_OPCODES.contains(abstractInsnNode.getOpcode())) {
            return false;
        }
        int index = ((VarInsnNode) abstractInsnNode).var;
        for (LocalVariableInfo localVariableInfo : createdVariables) {
            if (localVariableInfo.getIndex() == index && localVariableInfo.getIsInScope()) {
                return true;
            }
        }
        return false;
    }
}
