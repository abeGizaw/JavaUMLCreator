package domain.checks;

import domain.*;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PLKTest {
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator();

    @Test
    public void runPLKIsFieldOfThisExpectNoMessage() {
        String classPath = "PLKMockTestClasses/IsFieldOfThis";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 0;

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    @Test
    public void runPLKIsFieldOfNotThisExpectOneMessage() {
        String classPath = "PLKMockTestClasses/IsFieldOfNotThis";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 1;
        CheckType expectedCheck = CheckType.PLK;
        String expectedMessage = createExpectedMessageText("method1", "testClass1.testClass2", "checkNotField");
        String expectedClasses = "domain/checks/PLKMockTestClasses/IsFieldOfNotThis";

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
        Assertions.assertEquals(expectedCheck, CheckType.PLK);
        Assertions.assertEquals(expectedMessage, actualMessages.get(0).getMessage());
        Assertions.assertEquals(expectedClasses, actualMessages.get(0).getClassesOfInterest());
    }

    @Test
    public void runPLKIsCreatedAndReturnsExpectNoMessage() {
        String classPath = "PLKMockTestClasses/IsCreatedAndReturns";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 0;

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    @Test
    public void runPLKIsParameterAndHasArgumentsExpectNoMessage() {
        String classPath = "PLKMockTestClasses/IsParameterAndHasArguments";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 0;

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    private String createExpectedMessageText(String methodName, String receiverName, String calledMethod) {
        return String.format("Method: %s; %s is an invalid receiver for %s", methodName, receiverName, calledMethod);
    }
}
