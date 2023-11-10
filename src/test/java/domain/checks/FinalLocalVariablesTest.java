package domain.checks;

import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FinalLocalVariablesTest {
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator();

    @Test
    public void runFinalLocalVariablesOneScopeTwoMethodsExpectMethod1XMethod2X() {
        String classPath = "FinalLocalVariablesMockTestClasses/OneScope";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        FinalLocalVariables finalLocalVariablesCheck = new FinalLocalVariables();

        int expectedNumMessages = 2;
        CheckType expectedCheckType = CheckType.FINAL_LOCAL_VARIABLES;
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(createExpectedMessageText("method1", "x"));
        expectedMessages.add(createExpectedMessageText("method2", "x"));
        String expectedClass = "domain/checks/FinalLocalVariablesMockTestClasses/OneScope";

        List<Message> actualMessages = finalLocalVariablesCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
        for (int i = 0; i < expectedMessages.size(); i++) {
            Message actualMessage = actualMessages.get(i);
            Assertions.assertEquals(expectedCheckType, actualMessage.getCheckType());
            Assertions.assertEquals(expectedMessages.get(i), actualMessage.getMessage());
            Assertions.assertEquals(expectedClass, actualMessage.getClassesOfInterest());
        }
    }

    @Test
    public void runFinalLocalVariablesMultipleScopesExpectMethod1ZWBD() {
        String classPath = "FinalLocalVariablesMockTestClasses/MultipleScopes";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        FinalLocalVariables finalLocalVariablesCheck = new FinalLocalVariables();

        int expectedNumMessages = 4;
        CheckType expectedCheckType = CheckType.FINAL_LOCAL_VARIABLES;
        Set<String> expectedMessages = new HashSet<>();
        expectedMessages.add(createExpectedMessageText("method1", "z"));
        expectedMessages.add(createExpectedMessageText("method1", "w"));
        expectedMessages.add(createExpectedMessageText("method1", "b"));
        expectedMessages.add(createExpectedMessageText("method1", "d"));
        String expectedClass = "domain/checks/FinalLocalVariablesMockTestClasses/MultipleScopes";

        List<Message> actualMessages = finalLocalVariablesCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
        for (int i = 0; i < expectedMessages.size(); i++) {
            Message actualMessage = actualMessages.get(i);
            Assertions.assertEquals(expectedCheckType, actualMessage.getCheckType());
            Assertions.assertTrue(expectedMessages.contains(actualMessage.getMessage()));
            Assertions.assertEquals(expectedClass, actualMessage.getClassesOfInterest());
        }
    }

    @Test
    public void runFinalLocalVariablesNoFinalExpectNoVariablesCanBeFinal() {
        String classPath = "FinalLocalVariablesMockTestClasses/NoFinal";
        MyClassNode myClassNode = creator.createMyClassNodeFromName(classPath);
        FinalLocalVariables finalLocalVariablesCheck = new FinalLocalVariables();

        int expectedNumMessages = 0;

        List<Message> actualMessages = finalLocalVariablesCheck.run(myClassNode);

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    private String createExpectedMessageText(String methodName, String variableName) {
        return String.format("Method: %s; %s can be final since its value is not changed.\n", methodName, variableName);
    }
}
