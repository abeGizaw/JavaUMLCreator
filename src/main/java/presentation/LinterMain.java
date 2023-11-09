package presentation;

import datasource.MessageSaver;
import datasource.Saver;
import domain.CheckType;
import domain.Linter;
import domain.Message;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LinterMain {

    private static String basePath;
    private static List<String> paths;

    public static void main(String[] args) {
        promptUserForDirectory();
        String outputPath = promptUserForOutputFileName();
        Set<CheckType> checks = promptUserForChecks();

        for (String p : paths) {
            System.out.println(p);
        }
        System.out.println("OUTPUT PATH: " + outputPath);

        for (CheckType type : checks) {
            System.out.println(type.toString());
        }
        List<Message> messages = lint(checks);
        prettyPrint(messages);
        saveToFile(messages, outputPath);

    }

    private static List<Message> lint(Set<CheckType> checks) {
        MyClassNodeCreator creator = new MyASMClassNodeCreator();
        Linter linter = new Linter(paths, creator);
        return linter.runSelectedChecks(checks);

    }


    private static void promptUserForDirectory() {
        String userInput = promptUser("Enter Directory/Package: ");
        Path startPath = Paths.get(userInput);
        paths = new ArrayList<>();
        try {
            Files.walk(startPath)
                    .filter(p -> p.toString().endsWith(".class"))
                    .forEach(file -> paths.add(file.toString()));
        } catch (IOException e) {
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

    private static Set<CheckType> promptUserForChecks() {

        Set<CheckType> allChecks = new HashSet<>();
        allChecks.addAll(promptUserForStyle());
        allChecks.addAll(promptUserForPatters());
        allChecks.addAll(promptUserForPrinciples());

        return allChecks;

    }

    private static Set<CheckType> promptUserForPrinciples() {
        String userInput = promptUser("Enter Principle Checks to run separated by comma: \n Favor Composition over Inheritance (FCOI) , PLK (PLK), Program to Interface not Implementation (PINI), ALL");

        Set<CheckType> styleChecks = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "FCOI":
                    styleChecks.add(CheckType.COMPOSITION_OVER_INHERITANCE);
                    break;
                case "PLK":
                    styleChecks.add(CheckType.PLK);
                    break;
                case "PINI":
                    styleChecks.add(CheckType.INTERFACE_OVER_IMPLEMENTATION);
                    break;
                case "ALL":
                    styleChecks.add(CheckType.COMPOSITION_OVER_INHERITANCE);
                    styleChecks.add(CheckType.PLK);
                    styleChecks.add(CheckType.INTERFACE_OVER_IMPLEMENTATION);
                    break;
                default:
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
                    promptUserForPrinciples();
            }


        }
        return styleChecks;
    }

    private static Set<CheckType> promptUserForPatters() {
        String userInput = promptUser("Enter Pattern Checks to run separated by comma: \n Strategy Pattern (SP), Adapter Pattern (AP) , Template Method Pattern (TMP), ALL");

        Set<CheckType> styleChecks = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "SP":
                    styleChecks.add(CheckType.STRATEGY_PATTERN);
                    break;
                case "AP":
                    styleChecks.add(CheckType.ADAPTER_PATTERN);
                    break;
                case "TMP":
                    styleChecks.add(CheckType.TEMPLATE_METHOD_PATTERN);
                    break;
                case "ALL":
                    styleChecks.add(CheckType.STRATEGY_PATTERN);
                    styleChecks.add(CheckType.ADAPTER_PATTERN);
                    styleChecks.add(CheckType.TEMPLATE_METHOD_PATTERN);
                    break;
                default:
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
                    promptUserForPatters();
            }
        }
        return styleChecks;
    }

    private static Set<CheckType> promptUserForStyle() {
        String userInput = promptUser("Enter Style Checks to run separated by comma: \n Naming Convention (NC), Final Local Variables (FLV), Hidden Fields (HF), ALL");

        Set<CheckType> styleChecks = new HashSet<>();
        String[] inputArray = userInput.split(",");

        for (String s : inputArray) {
            switch (s.toUpperCase()) {
                case "NC":
                    styleChecks.add(CheckType.NAMING_CONVENTION);
                    break;
                case "FLV":
                    styleChecks.add(CheckType.FINAL_LOCAL_VARIABLES);
                    break;
                case "HF":
                    styleChecks.add(CheckType.HIDDEN_FIELDS);
                    break;
                case "ALL":
                    styleChecks.add(CheckType.NAMING_CONVENTION);
                    styleChecks.add(CheckType.FINAL_LOCAL_VARIABLES);
                    styleChecks.add(CheckType.HIDDEN_FIELDS);
                    break;
                default:
                    System.out.println("Invalid Input. Please Enter Abbreviations. ");
                    promptUserForStyle();
            }


        }
        return styleChecks;
    }

    private static String promptUser(String prompt) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println(prompt);
        return keyboard.nextLine();
    }


    private static void prettyPrint(List<Message> messages) {
        for(Message message: messages){
            System.out.println(message.toString());
        }
    }

    private static void saveToFile(List<Message> messages, String outputPath) {
        Saver saver = new MessageSaver(outputPath);
        for(Message message: messages){
            saver.saveMessage(message.toString());
        }

    }


}
