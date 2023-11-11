package datasource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ByteCodeExporter implements Exporter {

    public void save(String outputPath, String className, byte[] bytecode){
        outputPath = outputPath + File.separator+ "output";
        createDirectory(outputPath);
        File classFile = new File(outputPath + File.separator + className + ".class");
        try (FileOutputStream fos = new FileOutputStream(classFile)) {
            fos.write(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirectory(String path){
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
