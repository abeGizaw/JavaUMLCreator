package domain.ariscode;

import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.checks.FavorCompOverInheritance;
import domain.checks.NamingConventionCheck;
import domain.checks.StrategyPattern;

import java.io.IOException;
import java.util.List;

public class ArisMain {
    private static MyClassNodeCreator creator = new MyClassNodeCreator();
    public static void main(String[] args) throws IOException {
        runCompositionOverInheritance();
        System.out.println();
        System.out.println();
        runNamingConevention();
        System.out.println();
        System.out.println();
        runStrategyPatternDetection();

//        Message m = new Message(CheckType.COMPOSITION_OVER_INHERITANCE, "THIS IS A TEST", "domain/ariscode/testclasses/catBad");
//        System.out.println(m.toString());

    }


    private static void runCompositionOverInheritance() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";

        MyClassNode classNode = creator.crateMyClassNodeFromName(className);
        FavorCompOverInheritance favorCompOverInheritance = new FavorCompOverInheritance();
        List<Message> messageList = favorCompOverInheritance.run(classNode);
        printMessages(messageList);

    }

    private static void runNamingConevention() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";

        MyClassNode classNode =  creator.crateMyClassNodeFromName(className);
        NamingConventionCheck namingConventionCheck = new NamingConventionCheck();
        List<Message> messageList = namingConventionCheck.run(classNode);
        printMessages(messageList);
    }


    private static void runStrategyPatternDetection() throws IOException {
        String className = "domain/ariscode/testclasses/catBad";
        MyClassNode classNode = creator.crateMyClassNodeFromName(className);
        StrategyPattern strategyPattern = new StrategyPattern(creator);
        List<Message> messageList = strategyPattern.run(classNode);
        printMessages(messageList);
    }

    private static void printMessages( List<Message> messageList){
        for (Message message : messageList) {
            System.out.println(message.toString());
        }
    }
}
