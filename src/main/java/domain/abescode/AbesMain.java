package domain.abescode;

import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.abescode.alevelfeature.ConvertASMToUML;
import domain.checks.FieldHiding;
import domain.checks.ProgramInterfaceNotImplementation;
import domain.checks.TemplateMethodPattern;
import domain.myasm.MyASMClassNodeCreator;

import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AbesMain {
    private static MyClassNodeCreator creator = new MyASMClassNodeCreator();

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter Directory/Package: ");
        String packagePath = keyboard.nextLine();
        Path startPath = Paths.get(packagePath);

        try {
            Files.walk(startPath)
                    .filter(p -> p.toString().endsWith(".class"))
                    .forEach(filePath -> processClassFile(filePath, startPath));
        } catch (IOException e) {
            System.err.println("Error walking the directory: " + e.getMessage());
        }

        ConvertASMToUML aLevel = new ConvertASMToUML();
        aLevel.run();
    }

    private static void processClassFile(Path filePath, Path startPath) {
        File file = filePath.toFile();
        String[] fileProperties = file.toString().split("\\\\");

        System.out.println("Looking through Class: " + fileProperties[fileProperties.length - 1] + " at: " + file);

        MyClassNode myClassNode  = creator.createMyClassNodeFromFile(file);
        FieldHiding fieldHider = new FieldHiding();
        List<Message> hiddenFields = fieldHider.run(myClassNode);
        for(Message message: hiddenFields){
            System.out.println(message.toString());
        }

        ProgramInterfaceNotImplementation designPrinciple = new ProgramInterfaceNotImplementation(creator, startPath);
        List<Message> badImplementation = designPrinciple.run(myClassNode);
        for(Message message: badImplementation){
            System.out.println(message.toString());
        }

        TemplateMethodPattern designPattern = new TemplateMethodPattern();
        List<Message> usesPattern = designPattern.run(myClassNode);
        for(Message message : usesPattern){
            System.out.println(message.toString());
        }

        System.out.println("\n");
    }
}
