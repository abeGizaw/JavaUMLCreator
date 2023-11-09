package domain.checks;

import domain.*;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetectUnusedFields implements Check {

    private final List<MyClassNode> classNodes;
    private final Map<MyFieldNode, Boolean> fieldUsageMap;
    private final Map<MyFieldNode, MyClassNode> fieldToClass;
    private final Map<String, MyFieldNode> nameToFieldNode;

    public DetectUnusedFields(List<MyClassNode> classNodes) {
        this.classNodes = classNodes;
        this.fieldToClass = new HashMap<>();
        this.fieldUsageMap = new HashMap<>();
        this.nameToFieldNode = new HashMap<>();
    }

    @Override
    public List<Message> run(MyClassNode classNode) {
        populateFieldMaps();
        detectAllUnusedFields(classNodes);

        return generateUnusedMessages();
    }

    private void detectAllUnusedFields(List<MyClassNode> classNodes) {
        for (MyClassNode classNode : classNodes) {
            for (MyMethodNode method : classNode.methods) {
                for (MyAbstractInsnNode instruction : method.instructions) {
                    if (instruction.getOpcode() == Opcodes.GETFIELD || instruction.getOpcode() == Opcodes.GETSTATIC) {
                        MyFieldInsnNode node = (MyFieldInsnNode) instruction;
                        MyFieldNode fieldNode = nameToFieldNode.get(node.name);
                        if (fieldNode != null) {
                            fieldUsageMap.put(fieldNode, true);
                        }
                    }
                }
            }
        }
    }

    private void populateFieldMaps() {
        for (MyClassNode classNode : classNodes) {
            for (MyFieldNode fieldNode : classNode.fields) {
                fieldUsageMap.put(fieldNode, false);
                fieldToClass.put(fieldNode, classNode);
                nameToFieldNode.put(fieldNode.name, fieldNode);
            }
        }
    }

    private List<String> getNamesToDelete() {
        List<String> namesToDelete = new ArrayList<>();
        for (MyFieldNode fieldNode : fieldUsageMap.keySet()) {
            if (!fieldUsageMap.get(fieldNode)) {
                namesToDelete.add(fieldNode.name);
            }
        }
        return namesToDelete;
    }

    private List<Message> generateUnusedMessages() {
        List<Message> messages = new ArrayList<>();
        for (MyFieldNode fieldNode : fieldUsageMap.keySet()) {
            if (!fieldUsageMap.get(fieldNode)) {
                String messageValue = String.format("%s is a field in %s that was not used and was remove.", fieldNode.name, fieldToClass.get(fieldNode).name);
                messages.add(new Message(CheckType.UNUSED_FIELD, messageValue, fieldToClass.get(fieldNode).name));
            }
        }
        return messages;
    }


}
