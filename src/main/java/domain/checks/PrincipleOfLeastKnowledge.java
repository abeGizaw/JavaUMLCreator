package domain.checks;

import domain.*;
import domain.myasm.MyASMType;

import java.util.*;

public class PrincipleOfLeastKnowledge implements Check {
    private static final Set<Integer> METHOD_OPCODES = Set.of(MyOpcodes.H_INVOKEVIRTUAL, MyOpcodes.H_INVOKESTATIC, MyOpcodes.H_INVOKESPECIAL, MyOpcodes.H_INVOKEINTERFACE);

    private Stack<MyAbstractInsnNode> instructionStack;

    public PrincipleOfLeastKnowledge() {
        instructionStack = new Stack<>();
    }

    public List<Message> run(MyClassNode myClassNode) {
        List<Message> messages = new ArrayList<>();
        for (MyMethodNode myMethodNode : myClassNode.methods) {
            List<String> messageTexts = checkMethod(myMethodNode);
            messages.addAll(createMessagesForMethod(messageTexts, myClassNode.name));
        }
        return messages;
    }

    private List<String> checkMethod(MyMethodNode myMethodNode) {
        List<String> messageTexts = new ArrayList<>();
        if ((myMethodNode.access & MyOpcodes.ACC_ABSTRACT) != 0 || myMethodNode.name.equals("<clinit>")) { // if it is not abstract and not a constructor for a constant
            return new ArrayList<>();
        }
        LocalVariableManager localVariableManager = new LocalVariableManager(myMethodNode);
        for (MyAbstractInsnNode myAbstractInsnNode : myMethodNode.instructions) {
            localVariableManager.updateVariableScopes(myAbstractInsnNode);
            int insnType = myAbstractInsnNode.getType();
            if (METHOD_OPCODES.contains(insnType)) {
                if (isConstructor(myAbstractInsnNode)) {
                    localVariableManager.addCreatedVariable(myAbstractInsnNode.getNext());
                } else {
                    String invalidReceiverNode = getInvalidReceiverNode(myAbstractInsnNode, localVariableManager);
                    if (!invalidReceiverNode.equals("")) {
                        messageTexts.add(String.format("Method: %s; %s is an invalid receiver for %s", myMethodNode.name, invalidReceiverNode, ((MyMethodInsnNode) myAbstractInsnNode).name));
                    }
                }
            } else {
                instructionStack.push(myAbstractInsnNode);
            }
        }
        return messageTexts;
    }

    private boolean isConstructor(MyAbstractInsnNode myAbstractInsnNode) {
        if (myAbstractInsnNode.getOpcode() != MyOpcodes.INVOKESPECIAL)  {
            return false;
        }
        return (((MyMethodInsnNode) myAbstractInsnNode).name).equals("<init>");
    }

    private String getInvalidReceiverNode(MyAbstractInsnNode myAbstractInsnNode, LocalVariableManager localVariableManager) {
        // remove arguments
        MyType myType = new MyASMType();
        MyType methodType = myType.getType(((MyMethodInsnNode) myAbstractInsnNode).desc);
        int numArguments = methodType.getArgumentTypes().length;
        for (int i = 0; i < numArguments; i++) {
            removeMethodArgument();
        }

        MyAbstractInsnNode receiverNode = instructionStack.pop();
        String receiverNodeName = "";

        // is field?
        if (receiverNode.getOpcode() == MyOpcodes.GETFIELD || receiverNode.getOpcode() == MyOpcodes.GETSTATIC) {
            MyAbstractInsnNode fieldsClassLoadInsn = instructionStack.pop();
            if ((fieldsClassLoadInsn.getOpcode() == MyOpcodes.ALOAD) && (((MyVarInsnNode) fieldsClassLoadInsn).var == 0)) {
                return "";
            }

            // get receiver name for invalid field
            receiverNodeName = ((MyFieldInsnNode) receiverNode).name;
            while (fieldsClassLoadInsn.getOpcode() == MyOpcodes.GETFIELD || fieldsClassLoadInsn.getOpcode() == MyOpcodes.GETSTATIC) {
                receiverNodeName = String.format("%s.%s", ((MyFieldInsnNode) fieldsClassLoadInsn).name, receiverNodeName);
                fieldsClassLoadInsn = instructionStack.pop();
            }
        }

        if (receiverNodeName.equals("")) {
            if (receiverNode.getType() != MyAbstractInsnNode.VAR_INSN) {
                return ""; // not a receiver
            }
            LocalVariableInfo receiver = localVariableManager.getVariableAtIndex(((MyVarInsnNode) receiverNode).var);
            if (receiver == null) {
                return ""; // the receiver is not a variable declared by the programmer (could be declared by something from Java)
            }
            receiverNodeName = receiver.getName();
        }

        // is created?
        if (localVariableManager.isCreatedVariable(receiverNode)) {
            return "";
        }

        // is parameter?
        if (localVariableManager.isParameter(receiverNode)) {
            return "";
        }

        // is this?
        if ((receiverNode.getType() == MyAbstractInsnNode.VAR_INSN) && (((MyVarInsnNode) receiverNode).var == 0)) {
            return "";
        }
        return receiverNodeName;
    }

    private void removeMethodArgument() {
        MyAbstractInsnNode abstractInsnNode = instructionStack.pop();
        int methodArgumentInsnOpcode = abstractInsnNode.getOpcode();
        while (methodArgumentInsnOpcode == MyOpcodes.GETFIELD || methodArgumentInsnOpcode == MyOpcodes.GETSTATIC) {
            abstractInsnNode = instructionStack.pop();
            methodArgumentInsnOpcode = abstractInsnNode.getOpcode();
        }
    }

    private List<Message> createMessagesForMethod(List<String> messageTexts, String className) {
        List<Message> messages = new ArrayList<>();
        for (String messageText : messageTexts) {
            Message message = new Message(LintType.PLK, messageText, className);
            messages.add(message);
        }
        return messages;
    }
}
