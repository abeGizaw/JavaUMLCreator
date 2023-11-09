package domain.arisCode;

import domain.CheckType;
import domain.Message;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QD_UnusedFields {
    private final List<ClassNode> classNodes;
    private final Map<FieldNode, Boolean> fieldUsageMap;
    private final Map<FieldNode, ClassNode> fieldToClass;
    private final Map<String, FieldNode> nameToFieldNode;
    private final List<ClassNode> modifiedClassNodes;
    private final String outputPath;

    public QD_UnusedFields(List<ClassNode> classNodes, String outputPath) {
        this.classNodes = classNodes;
        this.fieldToClass = new HashMap<>();
        this.fieldUsageMap = new HashMap<>();
        this.nameToFieldNode = new HashMap<>();
        this.modifiedClassNodes = new ArrayList<>();
        this.outputPath = outputPath;
    }

    public List<Message> run() {
        populateFieldMaps();
        detectAllUnusedFields(classNodes);

        for (ClassNode classNode : classNodes) {
            ClassNode modifiedClassNode = deleteUnusedFields(classNode);
            modifiedClassNodes.add(modifiedClassNode);
            boolean removedAllFields = checkIfAllDeleted(modifiedClassNode, getNamesToDelete());
            if (!removedAllFields) {
                throw new RuntimeException("UNUSED FIELDS REMAIN");
            }
        }
        exportModifiedClassNode();
        return generateUnusedMessages();
    }

    private void detectAllUnusedFields(List<ClassNode> classNodes) {
        for (ClassNode classNode : classNodes) {
            for (MethodNode method : classNode.methods) {
                for (AbstractInsnNode instruction : method.instructions) {
                    if (instruction.getOpcode() == Opcodes.GETFIELD || instruction.getOpcode() == Opcodes.GETSTATIC) {
                        FieldInsnNode node = (FieldInsnNode) instruction;
                        FieldNode fieldNode = nameToFieldNode.get(node.name);
                        if (fieldNode != null) {
                            fieldUsageMap.put(fieldNode, true);
                        }
                    }
                }
            }
        }
    }

    private List<Message> generateUnusedMessages() {
        List<Message> messages = new ArrayList<>();
        for (FieldNode fieldNode : fieldUsageMap.keySet()) {
            if (!fieldUsageMap.get(fieldNode)) {
                String messageValue = String.format("%s is a field in %s that was not used and was remove.", fieldNode.name, fieldToClass.get(fieldNode).name);
                messages.add(new Message(CheckType.UNUSED_FIELD, messageValue, fieldToClass.get(fieldNode).name));
            }
        }
        return messages;
    }

    private void populateFieldMaps() {
        for (ClassNode classNode : classNodes) {
            for (FieldNode fieldNode : classNode.fields) {
                fieldUsageMap.put(fieldNode, false);
                fieldToClass.put(fieldNode, classNode);
                nameToFieldNode.put(fieldNode.name, fieldNode);
            }
        }
    }

    private ClassNode deleteUnusedFields(ClassNode classNode) {
        List<String> fieldsToDelete = getNamesToDelete();
        ClassNode modifiedClassNode = new ClassNode();
        QD_FieldsRemover fieldsDeleter = new QD_FieldsRemover(Opcodes.ASM8, modifiedClassNode, fieldsToDelete);
        classNode.accept(fieldsDeleter);
        return modifiedClassNode;
    }

    private boolean checkIfAllDeleted(ClassNode classNode, List<String> namesToDelete) {
        for (FieldNode fieldNode : classNode.fields) {
            if (namesToDelete.contains(fieldNode.name)) {
                return false;
            }
        }

        for (MethodNode method : classNode.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction.getOpcode() == Opcodes.PUTFIELD || instruction.getOpcode() == Opcodes.PUTSTATIC) {
                    FieldInsnNode node = (FieldInsnNode) instruction;
                    if (namesToDelete.contains(node.name)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private List<String> getNamesToDelete() {
        List<String> namesToDelete = new ArrayList<>();
        for (FieldNode fieldNode : fieldUsageMap.keySet()) {
            if (!fieldUsageMap.get(fieldNode)) {
                namesToDelete.add(fieldNode.name);
            }
        }
        return namesToDelete;
    }

    private void exportModifiedClassNode() {
        for (ClassNode classNode : modifiedClassNodes) {
            String[] nameArray = classNode.name.split("/");
            ClassNodeToFile QDClassNodeToFile = new ClassNodeToFile(outputPath);
            QDClassNodeToFile.writeClassNodeToFile(classNode);

        }
    }
}
