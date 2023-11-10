package domain.checks;

import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    private String createExpectedMessageText(String methodName, String variableName) {
        return String.format("Method: %s; %s can be final since its value is not changed.\n", methodName, variableName);
    }
}
