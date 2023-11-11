package domain.checks;

import domain.LintType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdapterPatternTest {
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator(Path.of(""));

    @Test
    public void runAdapterPatternAbstractTargetExpectOneAdapterPattern() {
        String directoryPath = "domain/checks/AdapterPatternMockTestClasses/ValidAbstractTarget/";
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
        LintType expectedCheckType = LintType.ADAPTER_PATTERN;
        String expectedMessage = String.format("There is a possible use of the Adapter Pattern with\n" +
                "\tadapter: %s\n" +
                "\ttarget: %s\n" +
                "\tadaptee: %s\n" +
                "\tclient: %s.\n", adapterPath, targetPath, adapteePath, clientPath);
        String expectedClasses = String.format("%s, %s, %s, %s", adapterPath, targetPath, adapteePath, clientPath);

        List<Message> actualMessages = adapterPatternCheck.run(myClassNodes.get(0));

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
        Assertions.assertEquals(expectedCheckType, actualMessages.get(0).getCheckType());
        Assertions.assertEquals(expectedMessage, actualMessages.get(0).getMessage());
        Assertions.assertEquals(expectedClasses, actualMessages.get(0).getClassesOfInterest());
    }

    @Test
    public void runAdapterPatternInterfaceTargetExpectTwoAdapterPatterns() {
        String directoryPath = "domain/checks/AdapterPatternMockTestClasses/ValidInterfaceTarget/";
        String adapteePath = String.format("%sAdaptee", directoryPath);
        String adapterPath = String.format("%sAdapter", directoryPath);
        String clientPath = String.format("%sClient", directoryPath);
        String targetPath = String.format("%sTarget", directoryPath);
        String otherClassPath = String.format("%sAdapterPatternConcreteClass", directoryPath);

        List<String> paths = new ArrayList<>();
        paths.add(adapteePath);
        paths.add(adapterPath);
        paths.add(clientPath);
        paths.add(targetPath);
        paths.add(otherClassPath);

        List<MyClassNode> myClassNodes = new ArrayList<>();
        for (String path : paths) {
            myClassNodes.add(creator.createMyClassNodeFromName(path));
        }

        AdapterPattern adapterPatternCheck = new AdapterPattern(myClassNodes);

        int expectedNumMessages = 2;
        LintType expectedCheckType = LintType.ADAPTER_PATTERN;

        String expectedMessage0 = createExpectedMessageText(adapterPath, targetPath, adapteePath, clientPath);
        String expectedMessage1 = createExpectedMessageText(adapterPath, targetPath, otherClassPath, clientPath);

        String expectedClasses0 = createExpectedClassesText(adapterPath, targetPath, adapteePath, clientPath);
        String expectedClasses1 = createExpectedClassesText(adapterPath, targetPath, otherClassPath, clientPath);

        List<Message> actualMessages = adapterPatternCheck.run(myClassNodes.get(0));

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());

        Assertions.assertEquals(expectedCheckType, actualMessages.get(0).getCheckType());
        Assertions.assertEquals(expectedMessage0, actualMessages.get(0).getMessage());
        Assertions.assertEquals(expectedClasses0, actualMessages.get(0).getClassesOfInterest());

        Assertions.assertEquals(expectedCheckType, actualMessages.get(1).getCheckType());
        Assertions.assertEquals(expectedMessage1, actualMessages.get(1).getMessage());
        Assertions.assertEquals(expectedClasses1, actualMessages.get(1).getClassesOfInterest());
    }

    @Test
    public void runAdapterPatternNoPatternsExpectNoAdapterPatterns() {
        String directoryPath = "domain/checks/AdapterPatternMockTestClasses/NoAdapterPattern/";
        String otherClassPath = String.format("%sOtherClass", directoryPath);
        String adapterPath = String.format("%sAdapter", directoryPath);
        String clientPath = String.format("%sClient", directoryPath);
        String targetPath = String.format("%sTarget", directoryPath);

        List<String> paths = new ArrayList<>();
        paths.add(otherClassPath);
        paths.add(adapterPath);
        paths.add(clientPath);
        paths.add(targetPath);

        List<MyClassNode> myClassNodes = new ArrayList<>();
        for (String path : paths) {
            myClassNodes.add(creator.createMyClassNodeFromName(path));
        }

        AdapterPattern adapterPatternCheck = new AdapterPattern(myClassNodes);

        int expectedNumMessages = 0;

        List<Message> actualMessages = adapterPatternCheck.run(myClassNodes.get(0));

        Assertions.assertEquals(expectedNumMessages, actualMessages.size());
    }

    private String createExpectedMessageText(String adapterPath, String targetPath, String adapteePath, String clientPath) {
        return String.format("There is a possible use of the Adapter Pattern with\n" +
                "\tadapter: %s\n" +
                "\ttarget: %s\n" +
                "\tadaptee: %s\n" +
                "\tclient: %s.\n", adapterPath, targetPath, adapteePath, clientPath);
    }

    private String createExpectedClassesText(String adapterPath, String targetPath, String adapteePath, String clientPath) {
        return String.format("%s, %s, %s, %s", adapterPath, targetPath, adapteePath, clientPath);
    }
}
