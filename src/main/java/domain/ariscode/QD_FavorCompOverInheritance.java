package domain.ariscode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class QD_FavorCompOverInheritance {

    public static void main(String[] args) throws IOException {
        String className = "domain/Cat";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        checkForInheritance(classNode);

    }

    public static void checkForInheritance(ClassNode classNode){
        System.out.println("Class: " + classNode.name);
        String superName = classNode.superName;
        String[] parts = classNode.superName.split("/");
        if (!parts[0].isEmpty() && !parts[0].equals("java") ){
            System.out.println("Consider composition instead of inheritance. This extends " + superName );
        }
        System.out.println();
    }
}
