package domain.checks;
import domain.CheckType;
import domain.Message;
import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldHidingTest {
    MyClassNodeCreator classNodeCreator = new MyASMClassNodeCreator();
    @Test
    public void validateHiddenFieldsCheck_withHiddenFieldsOfDiffTypes_Expect4Hidden(){
        Path basePath= Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\FieldHidingMockClasses\\HiddenFieldsVaryTypes.class");
        validate(basePath, new ArrayList<>(Arrays.asList(
                "Field similarName is hidden by method doNothing",
                "Field anotherSimilar is hidden by method doMoreNothing",
                "Field similarName is hidden by method doMoreNothing",
                "Field slightlySimilar is hidden by method doMoreNothing")));
    }

    @Test
    public void validateHiddenFieldsCheck_withHiddenFieldsOfSameTypes_Expect4Hidden(){
        Path basePath= Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\FieldHidingMockClasses\\HiddenFieldsSameTypes.class");
        validate(basePath, new ArrayList<>(Arrays.asList(
                "Field similarName is hidden by method doNothing",
                "Field anotherSimilar is hidden by method doMoreNothing",
                "Field similarName is hidden by method doMoreNothing",
                "Field slightlySimilar is hidden by method doMoreNothing")));
    }

    @Test
    public void validateHiddenFieldsCheck_withNoHiddenFields_ExpectNoMessages(){
        Path basePath= Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\FieldHidingMockClasses\\NoHiddenFields.class");
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(basePath.toFile());
        FieldHiding fieldHidingCheck = new FieldHiding();
        List<Message> hiddenFields = fieldHidingCheck.run(classNode);
        assertEquals(0, hiddenFields.size());
    }

    private void validate(Path basePath, List<String> expectedMessages){
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(basePath.toFile());
        FieldHiding fieldHidingCheck = new FieldHiding();
        List<Message> hiddenFields = fieldHidingCheck.run(classNode);
        for(int i = 0; i < hiddenFields.size(); i ++){
            Message hiddenField = hiddenFields.get(i);
            assertEquals(hiddenField.getCheckType(), CheckType.HIDDEN_FIELDS);
            assertEquals(hiddenField.getClassOfInterest(), classNode.name);
            assertEquals(hiddenField.getMessage(), expectedMessages.get(i));
        }

    }
}
