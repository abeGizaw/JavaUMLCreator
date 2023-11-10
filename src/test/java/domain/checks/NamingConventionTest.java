package domain.checks;

import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NamingConventionTest {

    /**
     * Naming Convention Check checks for the following
     * - Class Names are in PascalCase
     * - Final static fields are in all caps
     * - All other fields are camelCase
     * - All method names are camelCase
     */
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator();

    @Test
    public void runValidNames() throws IOException {
        String className = "domain/checks/NamingConventionMockTestClasses/ValidNaming";

        MyClassNode classNode = creator.createMyClassNodeFromName(className);
        NamingConventionCheck namingConventionCheck = new NamingConventionCheck();
        List<Message> messageList = namingConventionCheck.run(classNode);
        printMessages(messageList);

        assertEquals(0, messageList.size());
    }

    private void runInvalid(String className, List<String> expectedMessages) {

        MyClassNode classNode = creator.createMyClassNodeFromName(className);
        NamingConventionCheck namingConventionCheck = new NamingConventionCheck();
        List<Message> messageList = namingConventionCheck.run(classNode);
        printMessages(messageList);

        for (int i = 0; i < messageList.size(); i++) {
            assertEquals(CheckType.NAMING_CONVENTION, messageList.get(i).getCheckType());
            assertEquals(className, messageList.get(i).getClassOfInterest());
            assertEquals(expectedMessages.get(i), messageList.get(i).getMessage());

        }
    }

    @Test
    public void runInvalidClassName() throws IOException {
        String className = "domain/checks/NamingConventionMockTestClasses/invalidClassName";
        String[] parts = className.split("/");
        String name = parts[parts.length - 1];
        String expectedMessage = "Invalid Name: Must be in PascalCase: " + name;
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(expectedMessage);

        runInvalid(className, expectedMessages);
    }

    @Test
    public void runInvalidAndValidFinalStaticField() throws IOException {
        String className = "domain/checks/NamingConventionMockTestClasses/FinalStaticFieldName";
        String fieldName = "invalidName";
        String fieldName2 = "InvalidName2";
        String expectedMessage = "Invalid Field Name: Static Final Fields must be in all caps:   " + fieldName;
        String expectedMessage2 = "Invalid Field Name: Static Final Fields must be in all caps:   " + fieldName2;

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(expectedMessage);
        expectedMessages.add(expectedMessage2);

        runInvalid(className, expectedMessages);
    }


    @Test
    public void runInvalidAndValidFieldNaming() throws IOException {
        String className = "domain/checks/NamingConventionMockTestClasses/FieldNaming";
        String fieldName = "INVALID_FIELD";
        String fieldName2 = "AlsoInvalid";
        String expectedMessage = "Invalid Field Name: Must be in camelCase:   " + fieldName;
        String expectedMessage2 = "Invalid Field Name: Must be in camelCase:   " + fieldName2;

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(expectedMessage);
        expectedMessages.add(expectedMessage2);

        runInvalid(className, expectedMessages);
    }


    @Test
    public void runInvalidAndValidMethodNaming() throws IOException {
        String className = "domain/checks/NamingConventionMockTestClasses/MethodNaming";
        String methodName = "INVALID_METHOD";
        String methodName2 = "AlsoInvalid";
        String expectedMessage = "Invalid method name: Must be in camelCase:  " + methodName;
        String expectedMessage2 = "Invalid method name: Must be in camelCase:  " + methodName2;

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(expectedMessage);
        expectedMessages.add(expectedMessage2);

        runInvalid(className, expectedMessages);
    }


    private static void printMessages(List<Message> messageList) {
        for (Message message : messageList) {
            System.out.println(message.toString());
        }
    }


}
