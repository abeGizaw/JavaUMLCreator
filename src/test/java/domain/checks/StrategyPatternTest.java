package domain.checks;

import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class StrategyPatternTest {
    private static final MyClassNodeCreator creator = new MyClassNodeCreator();

    @Test
    public void runStrategyPatternDetection() throws IOException {
        String className = "domain/checks/StrategyPatternMockTestClasses/catBad";
        MyClassNode classNode = creator.crateMyClassNodeFromName(className);
        StrategyPattern strategyPattern = new StrategyPattern(creator);
        List<Message> messageList = strategyPattern.run(classNode);
        printMessages(messageList);
        assertEquals(className, messageList.get(0).getClassOfInterest());
        assertEquals(CheckType.STRATEGY_PATTERN, messageList.get(0).getCheckType());
        String strategyClassName = "Ldomain/checks/StrategyPatternMockTestClasses/Furniture;";
        String fieldName = "strategyFurniture";
        String setterName ="setFurnitureStrat";
        String expectedMessage = String.format("STRATEGY PATTERN: %s stores an instance of  %s in the field %s. The setter is %s.\n", className, strategyClassName, fieldName, setterName);
        assertEquals(expectedMessage, messageList.get(0).getMessage());
    }

    private static void printMessages( List<Message> messageList){
        for (Message message : messageList) {
            System.out.println(message.toString());
        }
    }

}
