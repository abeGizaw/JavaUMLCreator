package domain.checks;

import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DetectUnusedFieldTest {
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator();

    @Test
    public void runDetect() throws IOException {
        String className1 = "domain/checks/UnusedFieldMockTestClasses/UnusedField";
        String className2 = "domain/checks/UnusedFieldMockTestClasses/UnusedFieldSupport";

        MyClassNode classNode1 = creator.createMyClassNodeFromName(className1);
        MyClassNode classNode2 = creator.createMyClassNodeFromName(className2);

        List<MyClassNode> classNodes = new ArrayList<>();
        classNodes.add(classNode1);
        classNodes.add(classNode2);
        Check detectUnusedFields = new DetectUnusedFields(classNodes);
        List<Message> messageList = detectUnusedFields.run(null);

        List<String> expectedClassNames = new ArrayList<>();
        expectedClassNames.add(className1);
        expectedClassNames.add(className2);

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(makeExpectedMessage("unusedAndNeverSet", className1));
        expectedMessages.add(makeExpectedMessage("unusedFieldSetInCons", className1));
        expectedMessages.add(makeExpectedMessage("unusedSupportLocal", className2));

        for (Message message: messageList){
            assertEquals(message.getCheckType(), CheckType.UNUSED_FIELD);
            assertTrue(expectedClassNames.contains(message.getClassesOfInterest()));
            assertTrue(expectedMessages.contains(message.getMessage()));
        }
    }

    private String makeExpectedMessage (String fieldName, String className){
       return String.format("%s is a field in %s that was not used and was remove.",fieldName , className);
    }
}


