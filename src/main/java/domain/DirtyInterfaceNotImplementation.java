package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DirtyInterfaceNotImplementation {

    public void run(){
        Scanner keyboard = new Scanner(System.in);
        // target/classes/domain/FieldMock.class
        System.out.print("Enter FilePath: ");
        String filePath = keyboard.nextLine();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ClassReader myReader = new ClassReader(fileInputStream);
            ClassNode myClassNode = new ClassNode();
            myReader.accept(myClassNode, ClassReader.EXPAND_FRAMES);



        } catch (IOException e) {
            System.err.println("Error reading class file");
        }
    }



}
