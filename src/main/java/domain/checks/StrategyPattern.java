package domain.checks;

import domain.*;
import domain.CheckType;
import domain.Message;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;


public class StrategyPattern implements Check {
    private final MyClassNodeCreator myClassNodeCreator;
    public StrategyPattern(MyClassNodeCreator creator){
        myClassNodeCreator = creator;
    }

    @Override
    public List<Message> run(MyClassNode classNode) {
        return checkForStrategyPattern(classNode);
    }
    private List<Message> checkForStrategyPattern(MyClassNode classNode) {
        List<Message> allMessages = new ArrayList<>();
        for (MyFieldNode field : classNode.fields) {
            String[] parts = field.desc.substring(1).split("/");
            if (!parts[0].equals("java") && !parts[0].isEmpty()) {
                Message  m = checkFieldForStrategyPattern(field, classNode);
                if(m!=null)
                    allMessages.add(m);
            }
        }

        return allMessages;
    }

    private Message checkFieldForStrategyPattern(MyFieldNode field, MyClassNode classNode) {
        if (fieldIsAbstractAndValidClass(field.desc.substring(1, (field.desc.length() - 1)))) {
            String setterName = findSetter(classNode, field.name, field.desc);
            if (!setterName.isEmpty()) {
                String messageValue = String.format("STRATEGY PATTERN: %s stores an instance of  %s in the field %s. The setter is %s. \n",
                        classNode.name, field.desc, field.name, setterName);
                return new Message(CheckType.STRATEGY_PATTERN, messageValue, classNode.name);

            }
        }
        return null;
    }

    private boolean fieldIsAbstractAndValidClass(String name) {
        MyClassNode myClassNode = myClassNodeCreator.crateMyClassNodeFromName(name);
        return fieldIsAbstractType(myClassNode);
    }

    private boolean fieldIsAbstractType(MyClassNode classNode) {
        return (((classNode.access & Opcodes.ACC_ABSTRACT) != 0) || ((classNode.access & Opcodes.ACC_INTERFACE) != 0));
    }

    private String findSetter(MyClassNode classNode, String fieldName, String fieldType) {
        for (MyMethodNode methodNode : classNode.methods) {
            for (MyAbstractInsnNode instruction : methodNode.instructions) {
//                MyFieldInsnNode fieldNode = new MyASMFieldInsnNode(instruction);
                if (instruction.getOpcode() == Opcodes.PUTFIELD || instruction.getOpcode() == Opcodes.PUTSTATIC) {
                    MyFieldInsnNode fieldInsn = (MyFieldInsnNode) instruction;
                    if (fieldInsn.name.equals(fieldName) && fieldInsn.desc.equals(fieldType)) {
                        return methodNode.name;
                    }
                }
            }
        }
        return "";
    }


}
