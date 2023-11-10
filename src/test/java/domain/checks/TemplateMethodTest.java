package domain.checks;
import domain.LintType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TemplateMethodTest {
    MyClassNodeCreator classNodeCreator = new MyASMClassNodeCreator(
            Path.of("").toAbsolutePath()
    );

    @Test
    public void validateTemplateMethodCheck_WithClassThatFollows_expectTrueMessage(){
        Path filePath = Path.of(
                "target/test-classes/domain/checks/TemplateMethodMockTestClasses/CorrectTemplateMethodMock.class"
        ).toAbsolutePath();

        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(filePath.toFile());
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern();
        List<Message> followsPattern = templateMethodPattern.run(classNode);

        assertEquals(1, followsPattern.size());
        Message pattern = followsPattern.get(0);
        assertEquals(LintType.TEMPLATE_METHOD_PATTERN, pattern.getCheckType());
        assertEquals(classNode.name, pattern.getClassesOfInterest());
        assertEquals("The Class "+ classNode.name + " uses the templateMethod Pattern", pattern.getMessage());
    }

    @Test
    public void validateTemplateMethodCheck_WithClassThatHasNoFinal_expectNoMessage(){
        Path filePath = Path.of(
                "target/test-classes/domain/checks/TemplateMethodMockTestClasses/NoFinalTemplateMethodMock.class"
        ).toAbsolutePath();
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(filePath.toFile());
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern();
        List<Message> followsPattern = templateMethodPattern.run(classNode);

        assertEquals(0, followsPattern.size());
    }

    @Test
    public void validateTemplateMethodCheck_WithClassThatHasNoAbstract_expectNoMessage(){
        Path filePath = Path.of(
                "target/test-classes/domain/checks/TemplateMethodMockTestClasses/NoAbstractTemplateMock.class"
        ).toAbsolutePath();
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(filePath.toFile());
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern();
        List<Message> followsPattern = templateMethodPattern.run(classNode);

        assertEquals(0, followsPattern.size());
    }

}
