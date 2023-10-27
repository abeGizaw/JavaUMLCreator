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
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QD_NamingConventionCheck {

    private static ClassReader reader;

    public static void main(String[] args) throws IOException {
        String className = "domain/Cat";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        checkClassName(classNode);
        checkFieldNames(classNode);
        checkMethodName(classNode);
//        printClass(classNode);
        printFields(classNode);
//        printMethods(classNode);

    }

    private static void checkClassName(ClassNode classNode) {
        String[] parts = classNode.name.split("/");
        String name = parts[parts.length - 1];
        System.out.println("Class: " + name);

        if (Character.isUpperCase(name.charAt(0))) {
            System.out.println("    Valid Name: " + name);
        } else {
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
                if (field.name.matches("[A-Z]+")){
                    System.out.println("    Valid field name: " + field.name);
                }else{

                }

            } else {
                if (Character.isLowerCase(field.name.charAt(0))) {
                    System.out.println("    Valid field name: " + field.name);
                } else {
                    System.out.println("    Invalid Field Name: Must start with a lowercase letter.   " + field.name);
                    invalidFliedNames.add(field);
                }
            }
        }
        System.out.println("    Total Fields with Naming Convention Violation:   " + invalidFliedNames.size());
        System.out.println();

    }

    private static void checkMethodName(ClassNode classNode) {
        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        List<MethodNode> invalidMethods = new ArrayList<>();
        System.out.println("Methods  ");
        for (MethodNode method : methods) {
//            if (method.desc)
            if (Character.isLowerCase(method.name.charAt(0)) || method.name.equals("<init>")) {
                System.out.println("    Valid method name: " + method.name);
            } else {
                System.out.println("    Invalid method name: Must be lowercase.  " + method.name);
                invalidMethods.add(method);
            }

        }
        System.out.println("    Total Invalid Method names: " + invalidMethods.size());

    }

    private static void printClass(ClassNode classNode) {
        System.out.println("Class's Internal JVM name: " + classNode.name);
        System.out.println("User-friendly name: "
                + Type.getObjectType(classNode.name).getClassName());
        System.out.println("public? "
                + ((classNode.access & Opcodes.ACC_PUBLIC) != 0));
        System.out.println("Extends: " + classNode.superName);
        System.out.println("Implements: " + classNode.interfaces);
        // TODO: how do I write a lint check to tell if this class has a bad name?
    }

    private static void printFields(ClassNode classNode) {
        // Print all fields (note the cast; ASM doesn't store generic data with its Lists)
        List<FieldNode> fields = (List<FieldNode>) classNode.fields;
        for (FieldNode field : fields) {
            System.out.println("	Field: " + field.name);
            System.out.println("	Internal JVM type: " + field.desc);
            System.out.println("	User-friendly type: "
                    + Type.getObjectType(field.desc).getClassName());
            // Query the access modifiers with the ACC_* constants.

            System.out.println("	public? "
                    + ((field.access & Opcodes.ACC_PUBLIC) != 0));
            // TODO: how do you tell if something has package-private access? (ie no access modifiers?)

            // TODO: how do I write a lint check to tell if this field has a bad name?

            System.out.println();
        }
    }

    private static void printMethods(ClassNode classNode) {
        List<MethodNode> methods = (List<MethodNode>) classNode.methods;
        for (MethodNode method : methods) {
            System.out.println("	Method: " + method.name);
            System.out
                    .println("	Internal JVM method signature: " + method.desc);

            System.out.println("	Return type: "
                    + Type.getReturnType(method.desc).getClassName());

            System.out.println("	Args: ");
            for (Type argType : Type.getArgumentTypes(method.desc)) {
                System.out.println("		" + argType.getClassName());
                // FIXME: what is the argument's *variable* name?
            }

            System.out.println("	public? "
                    + ((method.access & Opcodes.ACC_PUBLIC) != 0));
            System.out.println("	static? "
                    + ((method.access & Opcodes.ACC_STATIC) != 0));
            // How do you tell if something has default access? (ie no access modifiers?)

            System.out.println();

            // Print the method's instructions
            printInstructions(method);
        }
    }

    private static void printInstructions(MethodNode methodNode) {
        InsnList instructions = methodNode.instructions;
        for (int i = 0; i < instructions.size(); i++) {

            // We don't know immediately what kind of instruction we have.
            AbstractInsnNode insn = instructions.get(i);

            // FIXME: Is instanceof the best way to deal with the instruction's type?
            if (insn instanceof MethodInsnNode) {
                // A method call of some sort; what other useful fields does this object have?
                MethodInsnNode methodCall = (MethodInsnNode) insn;
                System.out.println("		Call method: " + methodCall.owner + " "
                        + methodCall.name);
            } else if (insn instanceof VarInsnNode) {
                // Some some kind of variable *LOAD or *STORE operation.
                VarInsnNode varInsn = (VarInsnNode) insn;
                int opCode = varInsn.getOpcode();
                // See VarInsnNode.setOpcode for the list of possible values of
                // opCode. These are from a variable-related subset of Java
                // opcodes.
            }
            // There are others...
            // This list of direct known subclasses may be useful:
            // http://asm.ow2.org/asm50/javadoc/user/org/objectweb/asm/tree/AbstractInsnNode.html

            // TODO: how do I write a lint check to tell if this method has a bad name?
        }
    }


}
