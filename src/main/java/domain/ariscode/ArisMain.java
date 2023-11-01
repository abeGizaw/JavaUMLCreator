package domain.ariscode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class ArisMain {

    public static void main(String[] args) throws IOException {
        runNamingConevention();
        runCompositionOverInheritance();
        runStrategyPatternDetection();
    }

    private static void runNamingConevention() throws IOException {
        String className = "domain/ariscode/testclasses/Chair";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        QD_NamingConventionCheck namingConventionCheck = new QD_NamingConventionCheck(classNode);
        namingConventionCheck.run();
    }


    private static void runCompositionOverInheritance() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        QD_FavorCompOverInheritance favorCompOverInheritance = new QD_FavorCompOverInheritance(classNode);
        favorCompOverInheritance.run();
    }

    private static void runStrategyPatternDetection() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";
        ClassReader reader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);

        QD_StrategyPattern strategyPattern = new QD_StrategyPattern(classNode);
        strategyPattern.run();
    }
}
