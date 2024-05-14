package domain.diagramconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static domain.constants.Constants.END_JSON_CHAR;

public class JsonFilesReader {

    private final String directoryPath;
    private final List<Map<String, String>> entries = new ArrayList<>();
    private List<String> jsonLines = new ArrayList<>();

    public JsonFilesReader(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<String> readJsonFilesInDirectory() {
        List<String> lines = new ArrayList<>();
        try {
            Files.list(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        try {
                            lines.addAll(readJsonFileLineByLine(p.toFile()));
                        } catch (IOException e) {
                            System.err.println("Error reading file: " + p);
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error listing files in directory: " + directoryPath);
        }
        return lines;
    }

    private List<String> readJsonFileLineByLine(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public void convertJsonToUML(StringBuilder classUmlContent) {

        classUmlContent.append("@startjson\n");
        for (int i = 0; i < jsonLines.size(); i ++){
            String line = jsonLines.get(i);
            if(line.contains(END_JSON_CHAR) && i < jsonLines.size() -2){
                classUmlContent.append(line).append(",\n");
                i += 2;
            } else {
                classUmlContent.append(line).append("\n");
            }
        }

        classUmlContent.append("@endjson\n");
    }


    public void parse(List<String> jsonLines) {
        this.jsonLines = jsonLines;
        if (jsonLines == null || jsonLines.isEmpty()) {
            return;
        }

        Map<String, String> currentEntry = null;
        for (String line : jsonLines) {
            line = line.trim();
            if (line.startsWith("{")) {
                currentEntry = new LinkedHashMap<>(); // Preserves the insertion order
            } else if (line.startsWith("}")) {
                if (currentEntry != null) {
                    entries.add(currentEntry);
                }
            } else if (line.contains(":")) {
                String[] parts = line.split(":");
                String key = parts[0].trim().replaceAll("\"", "");
                String value = parts[1].trim().replaceAll("\"", "").replaceAll(",", "");
                if (currentEntry != null) {
                    currentEntry.put(key, value);
                }
            }
        }
    }

}