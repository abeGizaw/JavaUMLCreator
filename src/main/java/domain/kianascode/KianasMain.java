package domain.kianascode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class KianasMain {
    public static void main(String[] args) throws IOException {
        String className = "domain/kianascode/FinalLocalVariablesTestClass";

        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        QDFinalLocalVariables finalLocalVariablesCheck = new QDFinalLocalVariables();
        finalLocalVariablesCheck.run(classNode);
    }
}
