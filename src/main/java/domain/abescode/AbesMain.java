//package domain.abescode;
//
//import domain.MyClassNode;
//import domain.abescode.alevelfeature.ConvertASMToUML;
//import domain.myasm.MyASMClassNode;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.nio.file.*;
//import java.io.IOException;
//import java.util.Scanner;
//
//public class AbesMain {
//    public static void main(String[] args) {
//        Scanner keyboard = new Scanner(System.in);
//        // target/classes/domain
//        System.out.print("Enter Directory/Package: ");
//        String packagePath = keyboard.nextLine();
//        Path startPath = Paths.get(packagePath);
//
//        try {
//            Files.walk(startPath)
//                    .filter(p -> p.toString().endsWith(".class"))
//                    .forEach(AbesMain::processClassFile);
//        } catch (IOException e) {
//            System.err.println("Error reading package \n");
//        }
//
//        ConvertASMToUML aLevel = new ConvertASMToUML();
//        // target/classes/domain/abescode/FieldMock.class
//        aLevel.run();
//    }
//
//    private static void processClassFile(Path filePath) {
//        MyClassNode myClassNode = new MyASMClassNode();
//        File file = filePath.toFile();
//        String[] fileProperties = file.toString().split("\\\\");
//
//        System.out.println("Looking through Class: " + fileProperties[fileProperties.length - 1] + " at: " + file);
//
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            MyClassReader myReader = new MyASMClassReader(fileInputStream);
//            myReader.accept(myClassNode);
//        } catch (IOException e) {
//            System.err.println("Error reading class file");
//        }
//
//
//        DirtyFieldHiding fieldHider = new DirtyFieldHiding();
//        fieldHider.run(myClassNode);
//
//        DirtyInterfaceNotImplementation designPrinciple = new DirtyInterfaceNotImplementation();
//        designPrinciple.run(myClassNode);
//
//        DirtyTemplateMethod designPattern = new DirtyTemplateMethod();
//        designPattern.run(myClassNode);
//
//        System.out.println("\n");
//    }
//}
