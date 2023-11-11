package domain.transformations;

import datasource.ByteCodeExporter;
import datasource.Exporter;
import domain.LintType;
import domain.Message;
import domain.MyClassNode;
import domain.MyOpcodes;
import domain.checks.DetectUnusedFields;
import domain.myasm.MyASMClassNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteUnusedFields implements Transformation {

    private final List<ClassNode> modifiedClassNodes;
    private final String outputPath;
    private final Exporter byteCodeExporter;

    public DeleteUnusedFields(String path) {
        this.modifiedClassNodes = new ArrayList<>();
        this.outputPath = path;
        this.byteCodeExporter = new ByteCodeExporter();
    }

    /**
     * Creates an instance of DetectUnusedFields to reuse the logic for detecting the unused fields.
     * This is not stored as the abstract type Check because we need access to the 1 method to get the names of the unused fields.
     * This is the best design so only raw data (Strings) are passed back.
     * This eliminates the check from knowing about FieldNodes.
     *
     * @param classNodes
     * @return
     */
    public List<Message> run(List<MyClassNode> classNodes) {
        DetectUnusedFields detectUnusedFields = new DetectUnusedFields(classNodes);
        detectUnusedFields.run(null);
        for (MyClassNode myClassNode : classNodes) {
            MyASMClassNode myASMClassNode = (MyASMClassNode) myClassNode;
            ClassNode modifiedClassNode = deleteUnusedFields(myASMClassNode.getClassNode(), detectUnusedFields.getNamesToDelete());
            modifiedClassNodes.add(modifiedClassNode);
        }
        return exportModifiedClassNodes();
    }

    private ClassNode deleteUnusedFields(ClassNode classNode, List<String> fieldsToDelete) {
        ClassNode modifiedClassNode = new ClassNode();
        FieldsRemover fieldsRemover = new FieldsRemover(MyOpcodes.ASM8, modifiedClassNode, fieldsToDelete);
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
            messages.add(new Message(LintType.UNUSED_FIELD, m, className));
        }
        return messages;
    }
}
