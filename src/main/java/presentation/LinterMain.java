package presentation;

import datasource.MessageSaver;
import datasource.Saver;
import domain.*;
import domain.myasm.MyASMClassNodeCreator;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class LinterMain {


    // need to add logic using the base path
    public static void main(String[] args) {
        Path directoryPath = promptUserForDirectory();
        List<String> files = parseDirectory(directoryPath);
        String outputPath = promptUserForOutputFileName();
        Set<LintType> checks = promptUserForChecks();
        Set<LintType> transformations =  promptUserForTransformations();

        for (String p : files) {
            System.out.println(p);
        }
        System.out.println("OUTPUT PATH: " + outputPath);

        for (LintType type : checks) {
            System.out.println(type.toString());
        }
        List<Message> messages = lint(checks, transformations, outputPath, files, directoryPath);
        prettyPrint(messages);
        saveToFile(messages, outputPath);

    }



    private static List<String> parseDirectory(Path directoryPath) {
        List<String> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(directoryPath)){
            stream.filter(p -> p.toString().endsWith(".class"))
                    .forEach(file -> files.add(file.toString()));
        } catch (Exception e) {
            System.err.println("Error walking through the directory: " + e.getMessage());
        }

        return files;
    }

    private static List<Message> lint(Set<LintType> checks, Set<LintType> transformations, String outputPath, List<String> files, Path directoryPath) {
        MyClassNodeCreator creator = new MyASMClassNodeCreator(directoryPath);
        Linter linter = new Linter(files, creator, outputPath);

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(linter.runSelectedTransformations(transformations));
        List<Message> messages = linter.runSelectedChecks(checks);
        allMessages.addAll(messages);
        return allMessages;
    }


    private static Path promptUserForDirectory() {
        String userInput = promptUser("Enter Directory/Package: ");
        if(!isValidPath(userInput)){
            System.err.println("Invalid package");
            return promptUserForDirectory();
        } else {
            return Path.of(userInput);
        }
    }

    public static boolean isValidPath(String inputPath) {
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


    private static String promptUserForOutputFileName() {
        return promptUser("Please enter an output file path");
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
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
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
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
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
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
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
                System.out.println("Invalid Input. Please Enter Abbreviations. ");
                promptUserForTransformations();
        }
        return transformations;
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

    private static void saveToFile(List<Message> messages, String outputPath) {
        Saver saver = new MessageSaver(outputPath);
        for (Message message : messages) {
            saver.saveMessage(message.toString());
        }

    }


}
