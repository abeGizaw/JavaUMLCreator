package domain.checks;

import domain.CheckType;
import domain.Message;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QD_UnusedFields {
    Map<String, Boolean> fieldUsageMap;

    public List<Message> run(List<ClassNode> classNodes) {
        List<Message> allMessages = new ArrayList<>();

        for (ClassNode classNode : classNodes) {
            List<Message> classMessages = checkClassForUnusedFields(classNode);
            allMessages.addAll(classMessages);
        }

        return allMessages;
    }

    private List<Message> checkClassForUnusedFields(ClassNode classNode) {
        List<Message> messages = new ArrayList<>();
        String[] parts = classNode.name.split("/");
        String className = parts[parts.length - 1];
        fieldUsageMap = new HashMap<>();

        for (FieldNode field : classNode.fields) {
            fieldUsageMap.put(field.name, false);
        }
        System.out.println(fieldUsageMap.toString());

        for (MethodNode method : classNode.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (isFieldInsnNode(instruction)) {
                    FieldInsnNode fieldInsn = (FieldInsnNode) instruction;
                    if (fieldUsageMap.containsKey(fieldInsn.name)) {
                        fieldUsageMap.put(fieldInsn.name, true);
                        System.out.printf(" %s    %s   %s   %s\n", fieldInsn.name, fieldInsn.desc, fieldInsn.owner, fieldInsn.getType(), ((FieldInsnNode) instruction).desc);
                    }
                }
            }
        }

        for (Map.Entry<String, Boolean> entry : fieldUsageMap.entrySet()) {
            if (!entry.getValue()) {
                String fieldName = entry.getKey();
                String messageValue = "Detected unused field: " + fieldName + " in class " + classNode.name;
                messages.add(new Message(CheckType.UNUSED_FIELD, messageValue, classNode.name));
            }
        }

        return messages;
    }


    private boolean isFieldInsnNode(AbstractInsnNode instruction) {
        int opcode = instruction.getOpcode();
        return opcode >= Opcodes.GETFIELD && opcode <= Opcodes.PUTFIELD;
    }

}
