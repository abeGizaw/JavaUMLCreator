package domain.checks;
import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TemplateMethodTest {
    MyClassNodeCreator classNodeCreator = new MyASMClassNodeCreator();

    @Test
    public void validateTemplateMethodCheck_WithClassThatFollows_expectTrueMessage(){
        Path filePath = Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\TemplateMethodMockTestClasses\\CorrectTemplateMethodMock.class");
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(filePath.toFile());
        TemplateMethodPattern templateMethodPattern = new TemplateMethodPattern();
        List<Message> followsPattern = templateMethodPattern.run(classNode);

        assertEquals(1, followsPattern.size());
        Message pattern = followsPattern.get(0);
        assertEquals(CheckType.TEMPLATE_METHOD_PATTERN, pattern.getCheckType());
        assertEquals(classNode.name, pattern.getClassOfInterest());
        assertEquals("The Class "+ classNode.name + " uses the templateMethod Pattern", pattern.getMessage());
    }

}
