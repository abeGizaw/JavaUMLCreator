package domain.abescode;

import domain.abescode.alevelfeature.ConvertASMToUML;

import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.util.Scanner;

public class AbesMain {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        // target/classes/domain
        System.out.print("Enter Directory/Package: ");
        String packagePath = keyboard.nextLine();
        Path startPath = Paths.get(packagePath);

        try {
            Files.walk(startPath)
                    .filter(p -> p.toString().endsWith(".class"))
                    .forEach(AbesMain::processClassFile);
        } catch (IOException e) {
            System.err.println("Error reading package \n");
        }


        ConvertASMToUML aLevel = new ConvertASMToUML();
        // target/classes/domain/abescode/FieldMock.class
        aLevel.run();
    }

    private static void processClassFile(Path filePath) {
        File file = filePath.toFile();
        String[] fileProperties = file.toString().split("\\\\");

        System.out.println("Looking through Class: " + fileProperties[fileProperties.length - 1] + " at: " + file);


        DirtyFieldHiding fieldHider = new DirtyFieldHiding();
        fieldHider.run(file);

        DirtyInterfaceNotImplementation designPrinciple = new DirtyInterfaceNotImplementation();
        designPrinciple.run(file);

        DirtyTemplateMethod designPattern = new DirtyTemplateMethod();
        designPattern.run(file);

        System.out.println("\n");


    }
}
