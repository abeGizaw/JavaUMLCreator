package presentation;

import datasource.LintResultSaver;
import datasource.Saver;
import domain.LintType;
import domain.Linter;
import domain.Message;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static domain.constants.Constants.*;

public class LinterMain {
    public static void main(String[] args) {
        Path directoryPath = promptUserForDirectory();
        List<String> files = parseDirectory(directoryPath);
        Map<String, Set<String>> packageToFile = generatePackageToFilePairing(directoryPath);
        String outputPath = promptUserForOutputFileName(OUTPUT_DIRECTORY_FOR_CHECKS);
        Set<LintType> checks = promptUserForChecks();
        Set<LintType> transformations = promptUserForTransformations();
        Map<LintType, String> diagrams = promptUserForDiagrams();

        MyClassNodeCreator creator = new MyASMClassNodeCreator(directoryPath);
        Linter linter = new Linter(files, creator, outputPath);
        List<Message> messages = lintForMessages(checks, transformations, linter);
        prettyPrint(messages);

        for (Map.Entry<String, Set<String>> entry : packageToFile.entrySet()) {
            System.out.println("Package: " + entry.getKey());
            for (String className : entry.getValue()) {
                System.out.println(" - " + className);
            }
        }

        Saver saver = new LintResultSaver(outputPath);
        saveMessagesToFile(messages, saver);
        generateAndSaveDiagramsToFile(linter, diagrams, saver);
    }

    private static List<Message> lintForMessages(Set<LintType> checks, Set<LintType> transformations, Linter linter) {
        List<Message> allMessages = new ArrayList<>(linter.runSelectedTransformations(transformations));
        List<Message> messages = linter.runSelectedChecks(checks);
        allMessages.addAll(messages);
        return allMessages;
    }

    private static Map<String, Set<String>> generatePackageToFilePairing(Path directoryPath) {
        Map<String, Set<String>> packageContents = new HashMap<>();

        try (Stream<Path> stream = Files.walk(directoryPath)) {
            List<Path> paths = stream
                    .filter(p -> p.toString().endsWith(".class"))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                Path packagePath = directoryPath.relativize(path.getParent());
                String packageName = packagePath.toString().replace(File.separator, ".");
                packageContents.putIfAbsent(packageName, new HashSet<>());
                String fileName = path.getFileName().toString();
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                packageContents.get(packageName).add(className);
            }
        } catch (Exception e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }

        return packageContents;
    }
    private static List<String> parseDirectory(Path directoryPath) {
        List<String> files = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(directoryPath)) {
            stream.filter(p -> p.toString().endsWith(".class"))
                    .forEach(file -> files.add(file.toString()));
        } catch (Exception e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }

        return files;
    }

    private static void generateAndSaveDiagramsToFile(Linter linter, Map<LintType, String> diagrams, Saver saver) {
        Map<StringBuilder, LintType> diagramBuilders = linter.generateDiagrams(diagrams.keySet());
        for(StringBuilder stringBuilder: diagramBuilders.keySet()){
            LintType lintType = diagramBuilders.get(stringBuilder);
            String fileOutput = diagrams.get(lintType);
            writeDiagramFiles(fileOutput, lintType, stringBuilder, saver);
        }
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

    private static Set<LintType> promptUserForChecks() {

        Set<LintType> allChecks = new HashSet<>();
        allChecks.addAll(promptUserForStyle());
        allChecks.addAll(promptUserForPatterns());
        allChecks.addAll(promptUserForPrinciples());

        return allChecks;

    }


    private static Set<LintType> promptUserForPrinciples() {
        String userInput = promptUser("Enter Principle Checks to run separated by comma: \n Favor Composition over Inheritance (FCOI) , PLK (PLK), Program to Interface not Implementation (PINI), ALL, NONE");

        Set<LintType> principles = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "FCOI":
                    principles.add(LintType.COMPOSITION_OVER_INHERITANCE);
                    break;
                case "PLK":
                    principles.add(LintType.PLK);
                    break;
                case "PINI":
                    principles.add(LintType.INTERFACE_OVER_IMPLEMENTATION);
                    break;
                case "ALL":
                    principles.add(LintType.COMPOSITION_OVER_INHERITANCE);
                    principles.add(LintType.PLK);
                    principles.add(LintType.INTERFACE_OVER_IMPLEMENTATION);
                    break;
                case "NONE":
                    break;
                default:
                    System.out.println(ABBREVIATION_ERROR);
                    promptUserForPrinciples();
            }


        }
        return principles;
    }

    private static Set<LintType> promptUserForPatterns() {
        String userInput = promptUser("Enter Pattern Checks to run separated by comma: \n Strategy Pattern (SP), Adapter Pattern (AP) , Template Method Pattern (TMP), ALL, NONE");

        Set<LintType> patterns = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "SP":
                    patterns.add(LintType.STRATEGY_PATTERN);
                    break;
                case "AP":
                    patterns.add(LintType.ADAPTER_PATTERN);
                    break;
                case "TMP":
                    patterns.add(LintType.TEMPLATE_METHOD_PATTERN);
                    break;
                case "ALL":
                    patterns.add(LintType.STRATEGY_PATTERN);
                    patterns.add(LintType.ADAPTER_PATTERN);
                    patterns.add(LintType.TEMPLATE_METHOD_PATTERN);
                    break;
                case "NONE":
                    break;
                default:
                    System.out.println(ABBREVIATION_ERROR);
                    promptUserForPatterns();
            }
        }
        return patterns;
    }

    private static Set<LintType> promptUserForStyle() {
        String userInput = promptUser("Enter Style Checks to run separated by comma: \n Naming Convention (NC), Final Local Variables (FLV), Hidden Fields (HF), Unused Fields (UF), ALL, NONE");

        Set<LintType> styleChecks = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "NC":
                    styleChecks.add(LintType.NAMING_CONVENTION);
                    break;
                case "FLV":
                    styleChecks.add(LintType.FINAL_LOCAL_VARIABLES);
                    break;
                case "HF":
                    styleChecks.add(LintType.HIDDEN_FIELDS);
                    break;
                case "ALL":
                    styleChecks.add(LintType.NAMING_CONVENTION);
                    styleChecks.add(LintType.FINAL_LOCAL_VARIABLES);
                    styleChecks.add(LintType.HIDDEN_FIELDS);
                    styleChecks.add(LintType.UNUSED_FIELD);
                    break;
                case "UF":
                    styleChecks.add(LintType.UNUSED_FIELD);
                    break;
                case "NONE":
                    break;
                default:
                    System.out.println(ABBREVIATION_ERROR);
                    promptUserForStyle();
            }
        }
        return styleChecks;
    }

    private static Set<LintType> promptUserForTransformations() {
        String userInput = promptUser("Enter Transformations to run: \n Remove Unused Fields (RUF), NONE");

        Set<LintType> transformations = new HashSet<>();

        switch (userInput.toUpperCase()) {
            case "RUF":
                transformations.add(LintType.UNUSED_FIELD);
            case "NONE":
                break;
            default:
                System.out.println(ABBREVIATION_ERROR);
                promptUserForTransformations();
        }
        return transformations;
    }

    private static Map<LintType, String> promptUserForDiagrams() {
        String userInput = promptUser("Enter Diagrams to generate: \n UML Class Diagram (UMLCLASS), NONE");

        Map<LintType, String> diagrams = new HashMap<>();

        switch (userInput.toUpperCase()) {
            case "UMLCLASS":
                diagrams.put(LintType.UML_CONVERTER, promptUserForOutputFileName(OUTPUT_FOR_PUML_CLASSDIAGRAM));
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


    private static void prettyPrint(List<Message> messages) {
        for (Message message : messages) {
            System.out.println(message.toString());
        }
    }

    private static void saveMessagesToFile(List<Message> messages, Saver saver) {
        for (Message message : messages) {
            saver.saveMessage(message.toString());
        }
    }
    private static void writeDiagramFiles(String fileOutput, LintType lintType, StringBuilder stringBuilder, Saver saver) {
        if(lintType == LintType.UML_CONVERTER){
            saver.writeToFile(stringBuilder.toString(), PUML_TYPE, fileOutput);
        }
    }
}
