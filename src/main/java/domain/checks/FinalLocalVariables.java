package domain.checks;

import domain.*;

import java.util.*;

public class FinalLocalVariables implements Check {
    private static final Set<Integer> STORE_OPCODES = Set.of(MyOpcodes.ISTORE, MyOpcodes.LSTORE, MyOpcodes.FSTORE, MyOpcodes.DSTORE, MyOpcodes.ASTORE);

    private LocalVariableManager localVariableManager;

    public List<Message> run(MyClassNode myClassNode) {
        List<Message> messages = new ArrayList<>();
        for (MyMethodNode myMethodNode : myClassNode.methods) {
            if ((myMethodNode.access & MyOpcodes.ACC_ABSTRACT) == 0 && !myMethodNode.name.equals("<clinit>")) { // if it is not abstract and not a constructor for a constant
                localVariableManager = new LocalVariableManager(myMethodNode);
                checkMethodForFinalLocalVariables(myMethodNode);
                messages.addAll(createMessagesForMethod(myClassNode.name, myMethodNode.name));
            }
        }
        return messages;
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

    private List<Message> createMessagesForMethod(String className, String methodName) {
        List<Message> messages = new ArrayList<>();
        for (LocalVariableInfo localVariableInfo : localVariableManager.getHasBeenStoredOnce()) {
            String messageText = String.format("Method: %s; %s can be final since its value is not changed.\n", methodName, localVariableInfo.getName());
            Message message = new Message(LintType.FINAL_LOCAL_VARIABLES, messageText, className);
            messages.add(message);
        }
        return messages;
    }
}
