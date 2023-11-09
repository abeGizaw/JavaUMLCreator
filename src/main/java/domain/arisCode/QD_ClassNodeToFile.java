package domain.checks;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QD_ClassNodeToFile {
    private final String outputPath;

    public QD_ClassNodeToFile(String outputPath) {
        this.outputPath = outputPath;
    }

    public void writeClassNodeToFile(ClassNode classNode) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);

        byte[] bytecode = classWriter.toByteArray();
        String[] classNameArray = classNode.name.split("/");
        String className = classNameArray[classNameArray.length - 1];
        System.out.println(className);
        File classFile = new File(outputPath + File.separator + className + ".class");
        try (FileOutputStream fos = new FileOutputStream(classFile)) {
            fos.write(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
