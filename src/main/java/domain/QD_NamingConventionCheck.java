package domain;
/*
  Note that this check is going to validate that proper naming convention is used for the following:
   - class names: UpperCase
   - field names: lowerCase
   - method names: lowerCase
   - final variable: all caps
 */

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QD_NamingConventionCheck {
    public static void main(String[] args) throws IOException {
        String className = "domain/Cat";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        checkClassName(classNode);
        checkFieldNames(classNode);
        checkMethodName(classNode);
    }

    private static void checkClassName(ClassNode classNode) {
        String[] parts = classNode.name.split("/");
        String name = parts[parts.length - 1];
        System.out.println("Class: " + name);

        if (!Character.isUpperCase(name.charAt(0))) {
            System.out.println("    Invalid Name: Must start with capital letter. " + name);
        }
        System.out.println();

    }

    private static void checkFieldNames(ClassNode classNode) {
        List<FieldNode> invalidFliedNames = new ArrayList<>();
        List<FieldNode> fields = (List<FieldNode>) classNode.fields;
        System.out.println("Fields");
        for (FieldNode field : fields) {

            if ((field.access & Opcodes.ACC_FINAL) != 0) {
                if (!field.name.matches("[A-Z]+")) {
                    System.out.println("    Invalid Field Name: Final Fields must be in all caps.   " + field.name);
                    invalidFliedNames.add(field);
                }

            } else {
                if (!Character.isLowerCase(field.name.charAt(0))) {
                    System.out.println("    Invalid Field Name: Must start with a lowercase letter.   " + field.name);
                    invalidFliedNames.add(field);
                }
            }
        }
        System.out.println("    Total Fields with Naming Convention Violation:   " + invalidFliedNames.size());
        System.out.println();
    }

    private static void checkMethodName(ClassNode classNode) {
        List<MethodNode> methods = classNode.methods;
        List<MethodNode> invalidMethods = new ArrayList<>();
        System.out.println("Methods  ");
        for (MethodNode method : methods) {
            if (!Character.isLowerCase(method.name.charAt(0)) && !method.name.equals("<init>")) {
                System.out.println("    Invalid method name: Must be lowercase.  " + method.name);
                invalidMethods.add(method);
            }
        }
        System.out.println("    Total Invalid Method names: " + invalidMethods.size());

    }
}
