package domain.transformations;

import datasource.ByteCodeExporter;
import datasource.Exporter;
import domain.LintType;
import domain.Message;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteUnusedFields implements Transformation {

    private final List<ClassNode> modifiedClassNodes;
    private final String outputPath;
    private final List<String> fieldsToDelete;

    private final Exporter byteCodeExporter;

    public DeleteUnusedFields(String path, List<String> fieldsToDelete) {
        this.modifiedClassNodes = new ArrayList<>();
        this.outputPath = path;
        this.fieldsToDelete = fieldsToDelete;
        this.byteCodeExporter = new ByteCodeExporter();
    }

    public List<Message> run(List<ClassNode> classNodes) {
        for (ClassNode classNode : classNodes) {
            ClassNode modifiedClassNode = deleteUnusedFields(classNode);
            modifiedClassNodes.add(modifiedClassNode);
        }
        return exportModifiedClassNodes();
    }

    private ClassNode deleteUnusedFields(ClassNode classNode) {
        ClassNode modifiedClassNode = new ClassNode();
        FieldsRemover fieldsRemover = new FieldsRemover(Opcodes.ASM8, modifiedClassNode, fieldsToDelete);
        classNode.accept(fieldsRemover);
        return modifiedClassNode;
    }

    private List<Message> exportModifiedClassNodes() {
       List<Message> messages = new ArrayList<>();
        for (ClassNode classNode : modifiedClassNodes) {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);

            byte[] bytecode = classWriter.toByteArray();
            String[] classNameArray = classNode.name.split("/");
            String className = classNameArray[classNameArray.length - 1];
            byteCodeExporter.save(outputPath, className, bytecode);
            String m = String.format("Exported Modifications to Remove Unused Fields to: %s\n", outputPath + File.separator + className + ".class");
            messages.add(new Message(LintType.UNUSED_FIELD,m ,className));
        }
        return messages;
    }
}
