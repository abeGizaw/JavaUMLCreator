package presentation;

import domain.DiagramType;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LinterTester extends LinterMain {
    public LinterTester() {
        super();
    }

    public void generateUMLFromPath(Path inputPath, String outputPath, String fileName) {
        Map<String, String> fileToPackage = parseDirectory(inputPath);
        Map<DiagramType, String> diagrams = new HashMap<>();
        diagrams.put(DiagramType.UML_CONVERTER, fileName);

        generateUMLFromData(inputPath, outputPath, fileToPackage, diagrams, "");
    }
}
