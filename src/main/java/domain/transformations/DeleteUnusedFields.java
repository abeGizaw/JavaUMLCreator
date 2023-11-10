package domain.transformations;

import datasource.ByteCodeExporter;
import datasource.Exporter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

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

    public void run(List<ClassNode> classNodes) {
        for (ClassNode classNode : classNodes) {
            ClassNode modifiedClassNode = deleteUnusedFields(classNode);
            modifiedClassNodes.add(modifiedClassNode);
        }
        exportModifiedClassNodes();
    }

    private ClassNode deleteUnusedFields(ClassNode classNode) {
        ClassNode modifiedClassNode = new ClassNode();
        FieldsRemover fieldsRemover = new FieldsRemover(Opcodes.ASM8, modifiedClassNode, fieldsToDelete);
        classNode.accept(fieldsRemover);
        return modifiedClassNode;
    }

    private void exportModifiedClassNodes() {
        for (ClassNode classNode : modifiedClassNodes) {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);

            byte[] bytecode = classWriter.toByteArray();
            String[] classNameArray = classNode.name.split("/");
            String className = classNameArray[classNameArray.length - 1];
            byteCodeExporter.save(outputPath, className, bytecode);
        }
    }
}
