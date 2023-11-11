package domain.checks;

import domain.*;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

public class PLKTest {
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator(Path.of(""));

    @Test
    public void runPLKIsFieldOfThisExpectNoMessage() {
        String classPath = "domain/checks/PLKMockTestClasses/IsFieldOfThis";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 0;

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    @Test
    public void runPLKIsFieldOfNotThisExpectOneMessage() {
        String classPath = "domain/checks/PLKMockTestClasses/IsFieldOfNotThis";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 1;
        LintType expectedCheck = LintType.PLK;
        String expectedMessage = createExpectedMessageText("method1", "testClass1.testClass2", "checkNotField");

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
        Assertions.assertEquals(expectedCheck, LintType.PLK);
        Assertions.assertEquals(expectedMessage, actualMessages.get(0).getMessage());
        Assertions.assertEquals(classPath, actualMessages.get(0).getClassesOfInterest());
    }

    @Test
    public void runPLKIsCreatedAndReturnsExpectNoMessage() {
        String classPath = "domain/checks/PLKMockTestClasses/IsCreatedAndReturns";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 0;

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    @Test
    public void runPLKIsParameterAndHasArgumentsExpectNoMessage() {
        String classPath = "domain/checks/PLKMockTestClasses/IsParameterAndHasArguments";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 0;

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    @Test
    public void runPLKIsReturnedExpectOneMessage() {
        String classPath = "domain/checks/PLKMockTestClasses/IsReturned";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        PrincipleOfLeastKnowledge plkCheck = new PrincipleOfLeastKnowledge();

        int expectedNumMessages = 1;
        LintType expectedCheck = LintType.PLK;
        String expectedMessage = createExpectedMessageText("method1", "testClass1", "checkReturned");

        List<Message> actualMessages = plkCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
        Assertions.assertEquals(expectedCheck, LintType.PLK);
        Assertions.assertEquals(expectedMessage, actualMessages.get(0).getMessage());
        Assertions.assertEquals(classPath, actualMessages.get(0).getClassesOfInterest());
    }

    @Test
    public void runPLKIsThisExpectNoMessage() {
        String classPath = "domain/checks/PLKMockTestClasses/IsThis";
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
