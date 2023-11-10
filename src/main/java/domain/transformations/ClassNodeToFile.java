package domain.transformations;

import datasource.ByteCodeExporter;
import datasource.Exporter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class ClassNodeToFile {
    private final String outputPath;
    private final Exporter byteCodeExporter;

    public ClassNodeToFile(String outputPath) {
        this.outputPath = outputPath;
        byteCodeExporter = new ByteCodeExporter();
    }

    public void writeClassNodeToFile(ClassNode classNode) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);

        byte[] bytecode = classWriter.toByteArray();
        String[] classNameArray = classNode.name.split("/");
        String className = classNameArray[classNameArray.length - 1];
        byteCodeExporter.save(outputPath, className, bytecode);
    }
}
