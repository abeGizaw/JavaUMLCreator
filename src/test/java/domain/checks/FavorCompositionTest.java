package domain.checks;

import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * FavorCompositionOverInheritance checks for the following things
 * - Violation = extends a class
 * Test: extends a class, does not extend
 */

public class FavorCompositionTest {

    private final MyClassNodeCreator creator = new MyASMClassNodeCreator();

    @Test
    public void runViolatesCompositionWithInheritance() throws IOException {
        String className = "domain/checks/FavorCompositionMockTestClasses/IsSubClass";
        String superName = "domain/checks/FavorCompositionMockTestClasses/SuperClass";

        MyClassNode classNode = creator.createMyClassNodeFromName(className);
        FavorCompOverInheritance compOverInheritance = new FavorCompOverInheritance();
        List<Message> messageList = compOverInheritance.run(classNode);
        printMessages(messageList);

        String expectedMessage = String.format("Consider composition instead of inheritance. " + classNode.name + " EXTENDS " + superName);

        assertEquals(className, messageList.get(0).getClassesOfInterest());
        assertEquals(CheckType.COMPOSITION_OVER_INHERITANCE, messageList.get(0).getCheckType());
        assertEquals(expectedMessage, messageList.get(0).getMessage());
    }

    @Test
    public void runNoViolationComposition() throws IOException {
        String className = "domain/checks/FavorCompositionMockTestClasses/ValidComp";

        MyClassNode classNode = creator.createMyClassNodeFromName(className);
        FavorCompOverInheritance compOverInheritance = new FavorCompOverInheritance();
        List<Message> messageList = compOverInheritance.run(classNode);
        printMessages(messageList);

        assertEquals(0, messageList.size());
    }

    private static void printMessages(List<Message> messageList) {
        for (Message message : messageList) {
            System.out.println(message.toString());
        }
    }
}
