package presentation;

import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.diagramconverter.ConvertASMToUML;
import domain.myasm.MyASMClassNodeCreator;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * @author gizawaa
 * This class is strictly to test the uml converter by itself. It is not part of the overall proejct, hence why it is not in the uml
 * This is a feature that I have decided I will keep working on, to see how accurate I can make it and try to use some better practices
 * to make the program more flexible
 * Again this project does not depend on this class AT ALL. This is for personal use over break.
 */
public class UMLMain {
    private static final MyClassNodeCreator creator = new MyASMClassNodeCreator(Path.of(""));
    private static StringBuilder umlBuilder = new StringBuilder();

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter Directory/Package: ");
        String packagePath = keyboard.nextLine();
        Path startPath = Paths.get(packagePath);

        umlBuilder.append("@startuml\n");

        try(Stream<Path> stream = Files.walk(startPath)){
            stream.filter(p -> p.toString().endsWith(".class"))
                    .forEach(UMLMain::processClassFile);
        } catch (IOException e) {
            System.err.println("Error walking the directory: " + e.getMessage());
        }

        umlBuilder.append("@enduml");
        try (FileWriter fileWriter = new FileWriter("output.puml")) {
            fileWriter.write(umlBuilder.toString());
        } catch (IOException e) {
            System.err.println("Error writing UML to output file");
        }
    }

    private static void processClassFile(Path filePath) {
//        File file = filePath.toFile();
//        String[] fileProperties = file.toString().split("\\\\");
//
//        System.out.println("Looking through Class: " + fileProperties[fileProperties.length - 1] + " at: " + file);
//
//        MyClassNode myClassNode  = creator.createMyClassNodeFromFile(file);
//        ConvertASMToUML ASMConverter = new ConvertASMToUML(new StringBuilder());
//        ASMConverter.generateDiagramByNode(myClassNode, umlBuilder);
//        umlBuilder.append("\n");
//        System.out.println("\n");
    }
}
