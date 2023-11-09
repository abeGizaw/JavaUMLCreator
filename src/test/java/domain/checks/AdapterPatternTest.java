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

public class AdapterPatternTest {
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator();

    @Test
    public void runAdapterPatternAbstractTargetExpectOneAdapterPattern() {
        String directoryPath = "AdapterPatternMockTestClasses/ValidAbstractTarget/";
        String adapteePath = String.format("%sAdaptee", directoryPath);
        String adapterPath = String.format("%sAdapter", directoryPath);
        String clientPath = String.format("%sClient", directoryPath);
        String targetPath = String.format("%sTarget", directoryPath);

        List<String> paths = new ArrayList<>();
        paths.add(adapteePath);
        paths.add(adapterPath);
        paths.add(clientPath);
        paths.add(targetPath);

        List<MyClassNode> myClassNodes = new ArrayList<>();
        for (String path : paths) {
            myClassNodes.add(creator.createMyClassNodeFromName(path));
        }

        AdapterPattern adapterPatternCheck = new AdapterPattern(myClassNodes);

        int expectedNumMessages = 1;
        CheckType expectedCheckType = CheckType.ADAPTER_PATTERN;
        String expectedAdapteePath = String.format("domain/checks/%s", adapteePath);
        String expectedAdapterPath = String.format("domain/checks/%s", adapterPath);
        String expectedClientPath = String.format("domain/checks/%s", clientPath);
        String expectedTargetPath = String.format("domain/checks/%s", targetPath);
        String expectedMessage = String.format("There is a possible use of the Adapter Pattern with\n" +
                "\tadapter: %s\n" +
                "\ttarget: %s\n" +
                "\tadaptee: %s\n" +
                "\tclient: %s.\n", expectedAdapterPath, expectedTargetPath, expectedAdapteePath, expectedClientPath);
        String expectedClasses = String.format("%s, %s, %s, %s", expectedAdapterPath, expectedTargetPath, expectedAdapteePath, expectedClientPath);

        List<Message> actualMessages = adapterPatternCheck.run(myClassNodes.get(0));

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
        Assertions.assertEquals(expectedCheckType, actualMessages.get(0).getCheckType());
        Assertions.assertEquals(expectedMessage, actualMessages.get(0).getMessage());
        Assertions.assertEquals(expectedClasses, actualMessages.get(0).getClassesOfInterest());
    }
}
