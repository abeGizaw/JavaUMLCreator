package domain;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DirtyFieldHiding {
    public void run() {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter FilePath (Style Check): ");
        // target/classes/domain/FieldMock.class
        String filePath = keyboard.nextLine();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ClassReader myReader = new ClassReader(fileInputStream);
            ClassNode myClassNode = new ClassNode();
            myReader.accept(myClassNode, ClassReader.EXPAND_FRAMES);

            List<String> hiddenFields = checkFieldHiding(myClassNode);

            System.out.println("The Fields that are hidden: " + hiddenFields);

        } catch (IOException e) {
            System.err.println("Error reading class file");
        }
    }

    private List<String> checkFieldHiding(ClassNode classNode) {
        Set<String> allFields = new HashSet<>();
        List<String> hiddenFields = new ArrayList<>();

        for (FieldNode field : classNode.fields) {
            allFields.add(field.name);
        }


        for (MethodNode method : classNode.methods) {
            if (method.localVariables != null) {
                for (LocalVariableNode variable : method.localVariables) {
                    if (allFields.contains(variable.name)) {
                        hiddenFields.add(variable.name);
                    }
                }
            }
        }



        return hiddenFields;
    }

    private static void reference(ClassNode classNode){
        System.out.println("Class Name: " + classNode.name);
        System.out.println("Class Version: " + classNode.version);
        System.out.println("Access: " + classNode.access);
        if ((classNode.access & Opcodes.ACC_PUBLIC) != 0) {
            System.out.println("The class is public.");
        }
        System.out.println("Super Name: " + classNode.superName);

        System.out.println("\nFields:");
        if (classNode.fields != null) {
            for (FieldNode field : classNode.fields) {
                System.out.println(" - " + field.name + ": " + field.desc);
            }
        }

        System.out.println("\nMethods:");
        if (classNode.methods != null) {
            for (MethodNode method : classNode.methods) {
                System.out.println(" - " + method.name + method.desc + " " + method.signature);


                if(method.localVariables != null){
                    System.out.println("Local vars: ");
                    for (LocalVariableNode variables : method.localVariables){
                        System.out.println(variables.name);
                    }
                }
            }
        }

        System.out.println("\nAttributes:");
        if (classNode.attrs != null) {
            for (Attribute attr : classNode.attrs) {
                System.out.println(" - " + attr.type);
            }
        }
    }
}
