package domain.ariscode;

import org.objectweb.asm.tree.ClassNode;

public class QD_FavorCompOverInheritance {

    private final ClassNode classNode;

    QD_FavorCompOverInheritance(ClassNode c) {
        classNode = c;
    }

    public void run() {
        checkForInheritance();
    }

    private void checkForInheritance() {
        System.out.println("Class: " + classNode.name);
        String superName = classNode.superName;
        String[] parts = classNode.superName.split("/");
        if (!parts[0].isEmpty() && !parts[0].equals("java")) {
            System.out.println("Consider composition instead of inheritance. This extends " + superName);
        }
        System.out.println();
    }
}
