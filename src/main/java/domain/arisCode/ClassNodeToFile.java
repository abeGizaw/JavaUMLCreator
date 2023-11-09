package domain.arisCode;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClassNodeToFile {
    private final String outputPath;

    public ClassNodeToFile(String outputPath) {
        this.outputPath = outputPath;
    }

    public void writeClassNodeToFile(ClassNode classNode) {
        // The classWriter and the accept method are ok to rely on 3rd party
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);

        byte[] bytecode = classWriter.toByteArray();
        String[] classNameArray = classNode.name.split("/");
        String className = classNameArray[classNameArray.length - 1];

        // This goes in the dataSource Layer
        File classFile = new File(outputPath + File.separator + className + ".class");
        try (FileOutputStream fos = new FileOutputStream(classFile)) {
            fos.write(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
