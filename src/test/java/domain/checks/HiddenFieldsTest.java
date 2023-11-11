package domain.checks;
import domain.LintType;
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

public class HiddenFieldsTest {
    MyClassNodeCreator classNodeCreator = new MyASMClassNodeCreator(
            Path.of("").toAbsolutePath()
    );
    @Test
    public void validateHiddenFieldsCheck_withHiddenFieldsOfDiffTypes_Expect4Hidden(){
        String className = "domain/checks/FieldHidingMockClasses/HiddenFieldsVaryTypes";
        validate(className, new ArrayList<>(Arrays.asList(
                "Field similarName is hidden by method doNothing",
                "Field anotherSimilar is hidden by method doMoreNothing",
                "Field similarName is hidden by method doMoreNothing",
                "Field slightlySimilar is hidden by method doMoreNothing")));
    }

    @Test
    public void validateHiddenFieldsCheck_withHiddenFieldsOfSameTypes_Expect4Hidden(){
        String className = "domain/checks/FieldHidingMockClasses/HiddenFieldsSameTypes";
        validate(className, new ArrayList<>(Arrays.asList(
                "Field similarName is hidden by method doNothing",
                "Field anotherSimilar is hidden by method doMoreNothing",
                "Field similarName is hidden by method doMoreNothing",
                "Field slightlySimilar is hidden by method doMoreNothing")));
    }

    @Test
    public void validateHiddenFieldsCheck_withNoHiddenFields_ExpectNoMessages(){
        String className = "domain/checks/FieldHidingMockClasses/NoHiddenFields";
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromName(className);
        HiddenFields hiddenFieldsCheck = new HiddenFields();
        List<Message> hiddenFields = hiddenFieldsCheck.run(classNode);
        assertEquals(0, hiddenFields.size());
    }

    private void validate(String basePath, List<String> expectedMessages){
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromName(basePath);
        HiddenFields hiddenFieldsCheck = new HiddenFields();
        List<Message> hiddenFields = hiddenFieldsCheck.run(classNode);
        for(int i = 0; i < hiddenFields.size(); i ++){
            Message hiddenField = hiddenFields.get(i);
            assertEquals(hiddenField.getCheckType(), LintType.HIDDEN_FIELDS);
            assertEquals(hiddenField.getClassesOfInterest(), classNode.name);
            assertEquals(hiddenField.getMessage(), expectedMessages.get(i));
        }

    }
}
