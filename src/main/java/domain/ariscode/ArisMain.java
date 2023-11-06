package domain.ariscode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArisMain {
    private static MyClassNodeCreator creator = new MyClassNodeCreator();
    public static void main(String[] args) throws IOException {
//        runCompositionOverInheritance();
//        System.out.println();
//        System.out.println();
//        runNamingConevention();
//        System.out.println();
//        System.out.println();
//        runStrategyPatternDetection();

        runDetectUnusedField();

    }


    private static void runCompositionOverInheritance() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";

        MyClassNode classNode = creator.crateMyClassNode(className);
        QD_FavorCompOverInheritance favorCompOverInheritance = new QD_FavorCompOverInheritance();
        List<Message> messageList = favorCompOverInheritance.run(classNode);
        printMessages(messageList);

    }

    private static void runNamingConevention() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";

        MyClassNode classNode =  creator.crateMyClassNode(className);
        QD_NamingConventionCheck namingConventionCheck = new QD_NamingConventionCheck();
        List<Message> messageList = namingConventionCheck.run(classNode);
        printMessages(messageList);
    }


    private static void runStrategyPatternDetection() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";
        MyClassNode classNode = creator.crateMyClassNode(className);
        QD_StrategyPattern strategyPattern = new QD_StrategyPattern(creator);
        List<Message> messageList = strategyPattern.run(classNode);
        printMessages(messageList);
    }

    private static void runDetectUnusedField(){
        String className = "domain/ariscode/testclasses/catBad";
        String className2 = "domain/ariscode/testclasses/Chair";
        ClassReader reader = null;
        ClassReader reader2 = null;
        ClassNode classNode = null;
        ClassNode classNode2 = null;
        try {
            reader = new ClassReader(className);
            reader2 = new ClassReader(className2);
            classNode = new ClassNode();
            classNode2 = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            reader2.accept(classNode2, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            throw new RuntimeException("This is not a valid class name: " + className);
        }

        QD_UnusedFields unusedFields = new QD_UnusedFields();
        List<ClassNode> classes = new ArrayList<>();
        classes.add(classNode);
        classes.add(classNode2);
        List<Message> messages =  unusedFields.run(classes);
        printMessages(messages);

    }

    private static void printMessages( List<Message> messageList){
        for (Message message : messageList) {
            System.out.println(message.toString());
        }
    }
}
