package datasource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteCodeExporter implements Exporter {

    public void save(String outputPath, String className, byte[] bytecode){
        // This goes in the dataSource Layer
        File classFile = new File(outputPath + File.separator + className + ".class");
        try (FileOutputStream fos = new FileOutputStream(classFile)) {
            fos.write(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
