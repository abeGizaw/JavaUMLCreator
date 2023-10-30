package domain.abescode;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.*;
import static presentation.ANSIColors.*;

public class DirtyFieldHiding {

    public void run(ClassNode myClassNode) {
        List<String> hiddenFields = checkFieldHiding(myClassNode);
        System.out.println(RED + "The Fields that are hidden: " + hiddenFields + RESET);
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
