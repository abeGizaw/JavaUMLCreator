package domain.abescode;

import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.abescode.alevelfeature.ConvertASMToUML;
import domain.checks.HiddenFields;
import domain.myasm.MyASMClassNodeCreator;

import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class AbesMain {
    private static final MyClassNodeCreator creator = new MyASMClassNodeCreator(Path.of(""));

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter Directory/Package: ");
        String packagePath = keyboard.nextLine();
        Path startPath = Paths.get(packagePath);

        try(Stream<Path> stream = Files.walk(startPath)){
            stream.filter(p -> p.toString().endsWith(".class"))
                    .forEach(AbesMain::processClassFile);
        } catch (IOException e) {
            System.err.println("Error walking the directory: " + e.getMessage());
        }

    }

    private static void processClassFile(Path filePath) {
        File file = filePath.toFile();
        String[] fileProperties = file.toString().split("\\\\");

        System.out.println("Looking through Class: " + fileProperties[fileProperties.length - 1] + " at: " + file);
        System.out.println("File is: " + file + " with path " + filePath);

        MyClassNode myClassNode  = creator.createMyClassNodeFromFile(file);
        ConvertASMToUML ASMConverter = new ConvertASMToUML();
        ASMConverter.run(myClassNode);
        System.out.println("\n");
    }
}
