package datasource;

import java.awt.event.WindowFocusListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static domain.constants.Constants.END_JSON_DIAGRAM;
import static domain.constants.Constants.START_JSON_DIAGRAM;

public class LintResultSaver implements Saver {
    private final String basePath;

    public LintResultSaver(String path) {
        this.basePath = path.endsWith(File.separator) ? path : path + File.separator;
        createDirectory(this.basePath);
    }

    public void saveMessage(String message) {
        System.out.println(message);
    }

    public void writeToFile(String info, String fileType, String outputPath) {
        String[] result = extractJson(info);
        String umlInfo = result[0];
        String jsonInfo = result[1];

        String jsonFilePath = basePath + File.separator + outputPath + "json" + fileType;
        String umlFilePath = basePath + File.separator + outputPath + fileType;

        // Writing to the UML file
        try (FileWriter fileWriter = new FileWriter(umlFilePath)) {
            fileWriter.write(umlInfo);
        } catch (IOException e) {
            System.err.println("Error writing " + fileType + " to output file " + umlFilePath);
        }

        // Writing to the JSON file
        if(!jsonInfo.isEmpty()){
            try (FileWriter fileWriter = new FileWriter(jsonFilePath)) {
                fileWriter.write(jsonInfo);
            } catch (IOException e) {
                System.err.println("Error writing " + fileType + " to output file " + jsonFilePath);
            }
        }
    }

    private static String[] extractJson(String input) {
        int startIndex = input.indexOf(START_JSON_DIAGRAM);
        int endIndex = input.indexOf(END_JSON_DIAGRAM);

        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
            return new String[]{input, ""}; // Keys not found, or endKey is before startKey
        }

        // Adjust endIndex to include the endKey
        endIndex += END_JSON_DIAGRAM.length();

        String extracted = input.substring(startIndex, endIndex);
        String remaining = input.substring(0, startIndex) + input.substring(endIndex);

        return new String[]{remaining, extracted};
    }

    private void createDirectory(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
