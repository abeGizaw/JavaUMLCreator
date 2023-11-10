package presentation;

import datasource.MessageSaver;
import datasource.Saver;
import domain.*;
import domain.myasm.MyASMClassNodeCreator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LinterMain {

    private static String basePath;
    private static List<String> paths;

    // need to add logic using the base path

    public static void main(String[] args) {
        promptUserForDirectory();
        String outputPath = promptUserForOutputFileName();
        Set<LintType> checks = promptUserForChecks();
        Set<LintType> transformations =  promptUserForTransformations();

        for (String p : paths) {
            System.out.println(p);
        }
        System.out.println("OUTPUT PATH: " + outputPath);

        for (LintType type : checks) {
            System.out.println(type.toString());
        }
        List<Message> messages = lint(checks, transformations, outputPath);
        prettyPrint(messages);
//        saveToFile(messages, outputPath);

    }

    private static List<Message> lint(Set<LintType> checks, Set<LintType> transformations, String outputPath) {
        MyClassNodeCreator creator = new MyASMClassNodeCreator();
        Linter linter = new Linter(paths, creator);
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(linter.runSelectedTransformations(transformations, outputPath));
        allMessages.addAll(linter.runSelectedChecks(checks));
        return allMessages;
    }


    private static void promptUserForDirectory() {
        String userInput = promptUser("Enter Directory/Package: ");
        Path startPath = Paths.get(userInput);
        try {
            paths = new ArrayList<>();
            Files.walk(startPath)
                    .filter(p -> p.toString().endsWith(".class"))
                    .forEach(file -> paths.add(file.toString()));
        } catch (Exception e) {
            System.err.println("Error reading package \n");
            promptUserForDirectory();
        }
        basePath = userInput;
    }


//    private static void processClassFile(Path filePath, String userInput) {
//        File file = filePath.toFile();
//
//        // Remove later
//        String[] fileProperties = file.toString().split("\\\\");
//        System.out.println("Looking through Class: " + fileProperties[fileProperties.length - 1] + " at: " + file);
//
//        MyClassNode myClassNode  = creator.createMyClassNodeFromFile(file);
//        DirtyFieldHiding fieldHider = new DirtyFieldHiding();
//        fieldHider.run(myClassNode);
//
//        DirtyInterfaceNotImplementation designPrinciple = new DirtyInterfaceNotImplementation(creator);
//        designPrinciple.run(myClassNode);
//
//        DirtyTemplateMethod designPattern = new DirtyTemplateMethod();
//        designPattern.run(myClassNode);
//
//        System.out.println("\n");
//
//
//    }

    private static String promptUserForOutputFileName() {
        return promptUser("Please enter an output file path");
    }

    private static Set<LintType> promptUserForChecks() {

        Set<LintType> allChecks = new HashSet<>();
        allChecks.addAll(promptUserForStyle());
        allChecks.addAll(promptUserForPatters());
        allChecks.addAll(promptUserForPrinciples());

        return allChecks;

    }



    private static Set<LintType> promptUserForPrinciples() {
        String userInput = promptUser("Enter Principle Checks to run separated by comma: \n Favor Composition over Inheritance (FCOI) , PLK (PLK), Program to Interface not Implementation (PINI), ALL, NONE");

        Set<LintType> styleChecks = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "FCOI":
                    styleChecks.add(LintType.COMPOSITION_OVER_INHERITANCE);
                    break;
                case "PLK":
                    styleChecks.add(LintType.PLK);
                    break;
                case "PINI":
                    styleChecks.add(LintType.INTERFACE_OVER_IMPLEMENTATION);
                    break;
                case "ALL":
                    styleChecks.add(LintType.COMPOSITION_OVER_INHERITANCE);
                    styleChecks.add(LintType.PLK);
                    styleChecks.add(LintType.INTERFACE_OVER_IMPLEMENTATION);
                    break;
                case "NONE":
                    break;
                default:
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
                    promptUserForPrinciples();
            }


        }
        return styleChecks;
    }

    private static Set<LintType> promptUserForPatters() {
        String userInput = promptUser("Enter Pattern Checks to run separated by comma: \n Strategy Pattern (SP), Adapter Pattern (AP) , Template Method Pattern (TMP), ALL, NONE");

        Set<LintType> styleChecks = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "SP":
                    styleChecks.add(LintType.STRATEGY_PATTERN);
                    break;
                case "AP":
                    styleChecks.add(LintType.ADAPTER_PATTERN);
                    break;
                case "TMP":
                    styleChecks.add(LintType.TEMPLATE_METHOD_PATTERN);
                    break;
                case "ALL":
                    styleChecks.add(LintType.STRATEGY_PATTERN);
                    styleChecks.add(LintType.ADAPTER_PATTERN);
                    styleChecks.add(LintType.TEMPLATE_METHOD_PATTERN);
                    break;
                case "NONE":
                    break;
                default:
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
                    promptUserForPatters();
            }
        }
        return styleChecks;
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
