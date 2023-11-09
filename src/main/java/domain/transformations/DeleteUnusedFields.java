package domain.transformations;

import domain.arisCode.ClassNodeToFile;
import domain.arisCode.QD_FieldsRemover;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

public class DeleteUnusedFields implements Transformation {

    private final List<ClassNode> modifiedClassNodes;
    private final String outputPath;

    public DeleteUnusedFields(String path) {
        this.modifiedClassNodes = new ArrayList<>();
        this.outputPath = path;
    }

    public void run(List<ClassNode> classNodes, List<String> fieldsToDelete) {
        for (ClassNode classNode : classNodes) {
            ClassNode modifiedClassNode = deleteUnusedFields(classNode, fieldsToDelete);
            modifiedClassNodes.add(modifiedClassNode);
            boolean removedAllFields = checkIfAllDeleted(modifiedClassNode, fieldsToDelete);
            if (!removedAllFields) {
                throw new RuntimeException("UNUSED FIELDS REMAIN");
            }
        }
        exportModifiedClassNodes();
    }

    private ClassNode deleteUnusedFields(ClassNode classNode, List<String> fieldsToDelete) {
        ClassNode modifiedClassNode = new ClassNode();
        QD_FieldsRemover fieldsDeleter = new QD_FieldsRemover(Opcodes.ASM8, modifiedClassNode, fieldsToDelete);
        classNode.accept(fieldsDeleter);
        return modifiedClassNode;
    }

    private void exportModifiedClassNodes() {
        for (ClassNode classNode : modifiedClassNodes) {
            ClassNodeToFile nodeToFile = new ClassNodeToFile(outputPath);
            nodeToFile.writeClassNodeToFile(classNode);
        }
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

}
