package datasource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LintResultSaver implements Saver {
    private PrintStream logStream;
    private String basePath;

    public LintResultSaver(String path) {
        this.basePath = path.endsWith(File.separator) ? path : path + File.separator;
        this.basePath += "output";
        createDirectory(path);
        try {
            logStream = new PrintStream(new FileOutputStream(this.basePath + File.separator + "linter_log.txt", false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveMessage(String message) {
        logStream.println(message);
    }

    public void writeToFile(String info, String fileType, String outputPath) {
        String filePath = basePath + File.separator + outputPath + fileType;
        try (FileWriter fileWriter = new FileWriter(filePath)){
            fileWriter.write(info);
        } catch (IOException e) {
            System.err.println("Error writing " + fileType + " to output file " + filePath);
        }
    }

    private void createDirectory(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
