package datasource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageSaver implements Saver {
    private PrintStream logStream;

    public MessageSaver(String path) {
        path = path + File.separator+ "output";
        createDirectory(path);
        try {
            logStream = new PrintStream(new FileOutputStream(String.format("%s/linter_log.txt", path), false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMessage(String message) {
        logStream.println(message);
    }

    private void createDirectory(String path){
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
