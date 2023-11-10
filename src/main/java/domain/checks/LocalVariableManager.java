package domain.checks;

import domain.*;

import java.util.HashSet;
import java.util.Set;

public class LocalVariableManager {
    private static final Set<Integer> STORE_OPCODES = Set.of(MyOpcodes.ISTORE, MyOpcodes.LSTORE, MyOpcodes.FSTORE, MyOpcodes.DSTORE, MyOpcodes.ASTORE);
    private static final Set<Integer> LOAD_OPCODES = Set.of(MyOpcodes.ILOAD, MyOpcodes.LLOAD, MyOpcodes.FLOAD, MyOpcodes.DLOAD, MyOpcodes.ALOAD);

    private Set<LocalVariableInfo> localVariables;
    private Set<LocalVariableInfo> parameters;
    private Set<LocalVariableInfo> createdVariables;
    private boolean prevInsnIsLabel;
    private MyAbstractInsnNode prevInsn;

    public LocalVariableManager(MyMethodNode myMethodNode) {
        localVariables = new HashSet<>();
        parameters = new HashSet<>();

        MyLabel startLabel = myMethodNode.localVariables.get(0).start.getLabel();
        for (MyLocalVariableNode myLocalVariableNode : myMethodNode.localVariables) {
            LocalVariableInfo newLocalVariable = new LocalVariableInfo(myLocalVariableNode.name, myLocalVariableNode.start.getLabel(), myLocalVariableNode.end.getLabel(), myLocalVariableNode.index);
            localVariables.add(newLocalVariable);
            if (myLocalVariableNode.index != 0 && myLocalVariableNode.start.getLabel().equals(startLabel)) {
                parameters.add(newLocalVariable);
            }
        }

        createdVariables = new HashSet<>();

        prevInsnIsLabel = false;
        prevInsn = null;
    }

    public void updateVariableScopes(MyAbstractInsnNode insn) {
        MyAbstractInsnNode nextInsn = insn.getNext();
        if (nextInsn != null && nextInsn.getType() == MyAbstractInsnNode.LABEL) {
            setInScopeVariables(nextInsn, localVariables);
        }

        if (prevInsnIsLabel) {
            setOutOfScopeVariables(prevInsn, localVariables);
            prevInsnIsLabel = false;
        }

        if (insn.getType() == MyAbstractInsnNode.LABEL) {
            prevInsnIsLabel = true;
            prevInsn = insn;
        }
    }

    private void setInScopeVariables(MyAbstractInsnNode insn, Set<LocalVariableInfo> localVariables) {
        MyLabel instructionLabel = ((MyLabelNode) insn).getLabel();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getFirstLabel().equals(instructionLabel)) {
                localVariableInfo.setIsInScope(true);
            }
        }
    }

    private void setOutOfScopeVariables(MyAbstractInsnNode insn, Set<LocalVariableInfo> localVariables) {
        MyLabel instructionLabel = ((MyLabelNode) insn).getLabel();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getLastLabel().equals(instructionLabel)) {
                localVariableInfo.setIsInScope(false);
            }
        }
    }

    public void addCreatedVariable(MyAbstractInsnNode myAbstractInsnNode) {
        if (myAbstractInsnNode == null || !STORE_OPCODES.contains(myAbstractInsnNode.getOpcode())) {
            return;
        }
        int index = ((MyVarInsnNode) myAbstractInsnNode).var;
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getIndex() == index && !localVariableInfo.getIsInScope()) { // !localVariableInfo.getIsInScope because the variable shouldn't have been created yet
                createdVariables.add(localVariableInfo);
            }
        }
    }

    public boolean isCreatedVariable(MyAbstractInsnNode abstractInsnNode) {
        if (!LOAD_OPCODES.contains(abstractInsnNode.getOpcode())) {
            return false;
        }
        int index = ((MyVarInsnNode) abstractInsnNode).var;
        for (LocalVariableInfo localVariableInfo : createdVariables) {
            if (localVariableInfo.getIndex() == index && localVariableInfo.getIsInScope()) {
                return true;
            }
        }
        return false;
    }

    public boolean isParameter(MyAbstractInsnNode abstractInsnNode) {
        if (!LOAD_OPCODES.contains(abstractInsnNode.getOpcode())) {
            return false;
        }
        int index = ((MyVarInsnNode) abstractInsnNode).var;
        for (LocalVariableInfo localVariableInfo : parameters) {
            if (localVariableInfo.getIndex() == index) {
                return true;
            }
        }
        return false;
    }

    public LocalVariableInfo getVariableAtIndex(int index) {
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getIsInScope() && localVariableInfo.getIndex() == index) {
                return localVariableInfo;
            }
        }
        return null;
    }

    public Set<LocalVariableInfo> getHasBeenStoredOnce() {
        Set<LocalVariableInfo> hasBeenStoredOnce = new HashSet<>();
        for (LocalVariableInfo localVariableInfo : localVariables) {
            if (localVariableInfo.getHasBeenStoredOnce()) {
                hasBeenStoredOnce.add(localVariableInfo);
            }
        }
        return hasBeenStoredOnce;
    }
}