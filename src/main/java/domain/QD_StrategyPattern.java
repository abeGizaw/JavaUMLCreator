package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.IOException;
import java.util.List;


public class QD_StrategyPattern {

    public static void main(String[] args) throws IOException {
        String className = "domain/Cat";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        checkForViolationOfStrategyPattern(classNode);

    }

    public static void checkForViolationOfStrategyPattern(ClassNode classNode) {
        List<FieldNode> fields = (List<FieldNode>) classNode.fields;
        for (FieldNode field : fields) {
            System.out.println("	Field: " + field.name);
            String[] parts = field.desc.substring(1).split("/");
            if (!parts[0].equals("java") && !parts[0].isEmpty())
                attemptToProcessClass(field.desc.substring(1, (field.desc.length() - 1)), classNode.name);
        }

    }

    private static void attemptToProcessClass(String name, String originalName) {
        System.out.println("Processing  " + name);

        ClassReader reader = null;
        try {
            reader = new ClassReader(name);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            processClassAsField(classNode, originalName);
        } catch (IOException e) {
            System.out.println("Could not Find: " + name);
        }
    }

    private static void processClassAsField(ClassNode classNode, String originalName) {
        System.out.println("Processing Class As Field: " + classNode.name);
//        System.out.println("	public? "
//                + ((classNode.access & Opcodes.ACC_PUBLIC) != 0));
//        System.out.println("	abstract? "
//                + ((classNode.access & Opcodes.ACC_ABSTRACT) != 0));
        if ((classNode.access & Opcodes.ACC_ABSTRACT) == 0){
            System.out.printf("Class %s has field of %s as a concrete class. To allow for proper use of strategy pattern try storing an abstract type as a field.\n", originalName, classNode.name);
        }
        System.out.println();
    }


}
