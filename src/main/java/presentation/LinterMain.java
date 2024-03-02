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
        String outputPath = promptUser(OUTPUT_DIRECTORY_FOR_CHECKS);

        Map<DiagramType, String> diagrams = promptUserForDiagrams();

        List<String> files = new ArrayList<>(fileToPackage.keySet());

        MyClassNodeCreator creator = new MyASMClassNodeCreator(directoryPath);
        Linter linter = new Linter(files, creator, outputPath, fileToPackage);

        Saver saver = new LintResultSaver(outputPath);
        generateAndSaveDiagramsToFile(linter, diagrams, saver);
    }

    /**
     * Generates and saves diagrams to files.
     * @param linter The linter to use for generating diagrams.
     * @param diagrams A map of diagram types to their respective output paths.
     * @param saver The saver object to use for writing diagrams to files.
     */
    private static void generateAndSaveDiagramsToFile(Linter linter, Map<DiagramType, String> diagrams, Saver saver) {
        Map<StringBuilder, DiagramType> diagramBuilders = linter.generateDiagrams(diagrams.keySet());
        for(StringBuilder stringBuilder: diagramBuilders.keySet()){
            DiagramType diagramType = diagramBuilders.get(stringBuilder);
            String fileOutput = diagrams.get(diagramType);
            writeDiagramFiles(fileOutput, diagramType, stringBuilder, saver);
        }
    }

    /**
     * Parses a directory and maps files to their respective packages.
     * @param directoryPath The path to the directory to parse.
     * @return A map of file paths to package names.
     */
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

    /**
     * Prompts the user for a directory and validates the input.
     * @return The path to the valid directory input by the user.
     */
    private static Path promptUserForDirectory() {
        String userInput = promptUser("Enter Directory/Package: ");
        if (!isValidPath(userInput)) {
            System.err.println(INVALID_PACKAGE);
            return promptUserForDirectory();
        } else {
            return Path.of(userInput);
        }
    }

    /**
     * Validates if the provided input path is valid.
     * @param inputPath The path to validate.
     * @return True if the path is valid, false otherwise.
     */
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


    /**
     * Prompts the user to select diagrams to generate.
     * @return A map of selected diagram types to their output file names.
     */
    private static Map<DiagramType, String> promptUserForDiagrams() {
        String userInput = promptUser("Enter Diagrams to generate: \n UML Class Diagram (UMLCLASS), NONE");

        Map<DiagramType, String> diagrams = new HashMap<>();

        switch (userInput.toUpperCase()) {
            case "UMLCLASS":
                diagrams.put(DiagramType.UML_CONVERTER, promptUser(OUTPUT_FOR_PUML_CLASSDIAGRAM));
            case "NONE":
                break;
            default:
                System.out.println(ABBREVIATION_ERROR);
                promptUserForDiagrams();
        }
        return diagrams;

    }

    /**
     * Prompts the user and returns their input.
     * @param prompt The message to display to the user.
     * @return The user input as a string.
     */
    private static String promptUser(String prompt) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println(prompt);
        return keyboard.nextLine();
    }

    /**
     * Writes the diagram files to the output destination.
     * @param fileOutput The output file name.
     * @param diagramType The type of the diagram.
     * @param stringBuilder The string builder containing the diagram.
     * @param saver The saver object to write the diagram to a file.
     */
    private static void writeDiagramFiles(String fileOutput, DiagramType diagramType, StringBuilder stringBuilder, Saver saver) {
        if(diagramType == DiagramType.UML_CONVERTER){
            saver.writeToFile(stringBuilder.toString(), PUML_TYPE, fileOutput);
        }
    }
}
