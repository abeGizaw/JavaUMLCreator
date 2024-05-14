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
        String jsonPackage;

        Path directoryPath = promptUserForDirectory(ASK_FOR_JSON_PATH);
        Map<String, String> fileToPackage = parseDirectory(directoryPath);

        boolean wantsDefault = promptUser(ASK_FOR_DEFAULT).equalsIgnoreCase("Y");
        String outputPath = wantsDefault ? "UMLOutput" : promptUser(OUTPUT_DIRECTORY_FOR_CHECKS);

        if(outputPath.contains(""+File.separatorChar)){
            System.out.println(ANSIColors.YELLOW + getWarningMessage(outputPath) + ANSIColors.RESET);
        }

        Map<DiagramType, String> diagrams = setupDiagrams(wantsDefault);


        Boolean includeJson = promptUserForJson();
        if (includeJson){
            jsonPackage = String.valueOf(promptUserForDirectory(JSON_PACKAGE));
        } else {
            jsonPackage = "";
        }

        generateUMLFromData(directoryPath, outputPath, fileToPackage, diagrams, jsonPackage);
    }

    private static Map<DiagramType, String> setupDiagrams(boolean wantsDefault) {
        if (wantsDefault) {
            Map<DiagramType, String> defaultDiagrams = new HashMap<>();
            defaultDiagrams.put(DiagramType.UML_CONVERTER, "UMLDiagram");
            return defaultDiagrams;
        } else {
            return promptUserForDiagrams();
        }
    }



    /**
     * Generates and saves diagrams to files.
     *
     * @param linter      The linter to use for generating diagrams.
     * @param diagrams    A map of diagram types to their respective output paths.
     * @param saver       The saver object to use for writing diagrams to files.
     * @param includeJson Generate Json
     */
    private static void generateAndSaveDiagramsToFile(Linter linter, Map<DiagramType, String> diagrams, Saver saver, String includeJson) {
        Map<StringBuilder, DiagramType> diagramBuilders = linter.generateDiagrams(diagrams.keySet(), includeJson);
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
    static Map<String, String> parseDirectory(Path directoryPath) {
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
    private static Path promptUserForDirectory(String pathType) {
        String userInput = promptUser(pathType);
        if (!isValidPath(userInput)) {
            System.err.println(INVALID_PACKAGE);
            return promptUserForDirectory(pathType);
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
        String userInput = promptUser("Enter Diagrams to generate: \n UML Class Diagram (UC), NONE");

        Map<DiagramType, String> diagrams = new HashMap<>();

        switch (userInput.toUpperCase()) {
            case "UC":
                String outputPath = getOutputPathPUML();
                diagrams.put(DiagramType.UML_CONVERTER, outputPath);
            case "NONE":
                break;
            default:
                System.out.println(ABBREVIATION_ERROR);
                return promptUserForDiagrams();
        }
        return diagrams;

    }

    private static String getOutputPathPUML() {
        boolean isValidPath;
        String outputPath;
        do {
            outputPath = promptUser(OUTPUT_FOR_PUML_CLASSDIAGRAM);
            isValidPath = !outputPath.contains(File.separator);
            if (!isValidPath){
                System.out.println(ANSIColors.RED + INVALID_PUML_PATH + ANSIColors.RESET);
            }
        } while (!isValidPath);

        return outputPath;

    }

    private static Boolean promptUserForJson() {
        String userInput = promptUser(CHOICE_FOR_JSON);

        return userInput.equalsIgnoreCase("Y");
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

    static void generateUMLFromData(Path inputPath, String outputPath, Map<String, String> fileToPackage, Map<DiagramType, String> diagrams, String jsonPackage) {
        List<String> files = new ArrayList<>(fileToPackage.keySet());

        MyClassNodeCreator creator = new MyASMClassNodeCreator(inputPath);
        Linter linter = new Linter(files, creator, fileToPackage);

        Saver saver = new LintResultSaver(outputPath);
        generateAndSaveDiagramsToFile(linter, diagrams, saver, jsonPackage);
    }


}
