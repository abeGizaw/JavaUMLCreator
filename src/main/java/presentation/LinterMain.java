package presentation;

import datasource.LintResultSaver;
import datasource.Saver;
import domain.DiagramType;
import domain.Linter;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static domain.constants.Constants.*;

public class LinterMain {

    public static void main(String[] args) {
        Path directoryPath = promptUserForDirectory();
        Map<String, String> fileToPackage = parseDirectory(directoryPath);
        String outputPath = promptUserForOutputFileName(OUTPUT_DIRECTORY_FOR_CHECKS);

        Map<DiagramType, String> diagrams = promptUserForDiagrams();

        List<String> files = new ArrayList<>(fileToPackage.keySet());

        MyClassNodeCreator creator = new MyASMClassNodeCreator(directoryPath);
        Linter linter = new Linter(files, creator, outputPath, fileToPackage);

        Saver saver = new LintResultSaver(outputPath);
        generateAndSaveDiagramsToFile(linter, diagrams, saver);
    }

    private static void generateAndSaveDiagramsToFile(Linter linter, Map<DiagramType, String> diagrams, Saver saver) {
        Map<StringBuilder, DiagramType> diagramBuilders = linter.generateDiagrams(diagrams.keySet());
        for(StringBuilder stringBuilder: diagramBuilders.keySet()){
            DiagramType diagramType = diagramBuilders.get(stringBuilder);
            String fileOutput = diagrams.get(diagramType);
            writeDiagramFiles(fileOutput, diagramType, stringBuilder, saver);
        }
    }

    private static Map<String, String> parseDirectory(Path directoryPath) {
        Map<String, String> fileToPackage = new HashMap<>();

        try (Stream<Path> stream = Files.walk(directoryPath)) {
            stream.filter(p -> p.toString().endsWith(".class"))
                    .forEach(path -> {
                        Path packagePath = directoryPath.relativize(path.getParent());
                        String packageName = packagePath.toString().replace(File.separator, ".");
                        fileToPackage.put(path.toString(), packageName);
                    });
        } catch (Exception e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }

        return fileToPackage;
    }

    private static Path promptUserForDirectory() {
        String userInput = promptUser("Enter Directory/Package: ");
        if (!isValidPath(userInput)) {
            System.err.println(INVALID_PACKAGE);
            return promptUserForDirectory();
        } else {
            return Path.of(userInput);
        }
    }

    private static boolean isValidPath(String inputPath) {
        if (inputPath == null || inputPath.isEmpty()) {
            return false;
        }

        Path path;
        try {
            path = Paths.get(inputPath);
        } catch (InvalidPathException e) {
            return false;
        }

        return Files.exists(path);
    }


    private static String promptUserForOutputFileName(String destinationMessage) {
        return promptUser(destinationMessage);
    }

    private static Map<DiagramType, String> promptUserForDiagrams() {
        String userInput = promptUser("Enter Diagrams to generate: \n UML Class Diagram (UMLCLASS), NONE");

        Map<DiagramType, String> diagrams = new HashMap<>();

        switch (userInput.toUpperCase()) {
            case "UMLCLASS":
                diagrams.put(DiagramType.UML_CONVERTER, promptUserForOutputFileName(OUTPUT_FOR_PUML_CLASSDIAGRAM));
            case "NONE":
                break;
            default:
                System.out.println(ABBREVIATION_ERROR);
                promptUserForDiagrams();
        }
        return diagrams;

    }
    private static String promptUser(String prompt) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println(prompt);
        return keyboard.nextLine();
    }

    private static void writeDiagramFiles(String fileOutput, DiagramType diagramType, StringBuilder stringBuilder, Saver saver) {
        if(diagramType == DiagramType.UML_CONVERTER){
            saver.writeToFile(stringBuilder.toString(), PUML_TYPE, fileOutput);
        }
    }
}
