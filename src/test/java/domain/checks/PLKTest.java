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
}
