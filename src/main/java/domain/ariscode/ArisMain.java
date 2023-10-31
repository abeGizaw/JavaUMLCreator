package domain.ariscode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class ArisMain {

    public static void main(String[] args) throws IOException {
        String className = "domain/ariscode/testclasses/Chair";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        QD_NamingConventionCheck namingConventionCheck = new QD_NamingConventionCheck(classNode);
        namingConventionCheck.run();



    }



}
