package domain.checks;

import domain.*;

import java.util.*;

public class FinalLocalVariables {
    private static final Set<Integer> STORE_OPCODES = Set.of(MyOpcodes.ISTORE, MyOpcodes.LSTORE, MyOpcodes.FSTORE, MyOpcodes.DSTORE, MyOpcodes.ASTORE);

    private LocalVariableManager localVariableManager;

    public void run(MyClassNode myClassNode) {
        for (MyMethodNode myMethodNode : myClassNode.methods) {
            System.out.printf("Method: %s\n", myMethodNode.name);
            localVariableManager = new LocalVariableManager(myMethodNode);
            checkMethodForFinalLocalVariables(myMethodNode);
            printResults();
        }
    }

    private void checkMethodForFinalLocalVariables(MyMethodNode myMethodNode) {
        for (MyAbstractInsnNode insn : myMethodNode.instructions) {
            localVariableManager.updateVariableScopes(insn);
            if (STORE_OPCODES.contains(insn.getOpcode())) {
                LocalVariableInfo varAtIndex = localVariableManager.getVariableAtIndex(((MyVarInsnNode) insn).var);
                if (varAtIndex != null) {
                    handleValueStored(varAtIndex);
                }
            }
        }
    }

    private void handleValueStored(LocalVariableInfo localVariableInfo) {
        if (localVariableInfo.getHasBeenStored()) {
            localVariableInfo.setHasBeenStoredOnce(false);
        } else {
            localVariableInfo.setHasBeenStored(true);
            localVariableInfo.setHasBeenStoredOnce(true);
        }
    }

    private void printResults() {
        for (LocalVariableInfo localVariableInfo : localVariableManager.getHasBeenStoredOnce()) {
            System.out.printf("%s can be final since the value is not changed.\n", localVariableInfo.getName());
        }
    }
}
