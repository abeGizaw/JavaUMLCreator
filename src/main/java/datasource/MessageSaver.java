package datasource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MessageSaver implements Saver {
    private PrintStream logStream;

    public MessageSaver(String path) {
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
}
