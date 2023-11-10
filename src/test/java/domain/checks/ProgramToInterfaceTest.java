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

public class ProgramToInterfaceTest {
    MyClassNodeCreator classNodeCreator = new MyASMClassNodeCreator();

    @Test
    public void validateProgramToInterface_withClassThatViolates_expectViolations(){
        Path filePath = Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\ProgramToInterfaceNotImplementationTestClasses\\BadInterfaceUse.class");
        validate(filePath, new ArrayList<>(Arrays.asList(
                "Where you need to Programming to interface instead of implementation: violatesPattern",
                "Where you need to Programming to interface instead of implementation: violator",
                "Where you need to Programming to interface instead of implementation: violatesDifferently",
                "Where you need to Programming to interface instead of implementation: badInterfaceUse",
                "Where you need to Programming to interface instead of implementation: badAbstractUse"
        )));
    }

    @Test
    public void validateProgramToInterface_withClassThatDoesNotViolate_expectEmptyMessage(){
        Path filePath = Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\ProgramToInterfaceNotImplementationTestClasses\\GoodInterfaceUse.class");
        Path packagePath = Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\ProgramToInterfaceNotImplementationTestClasses");
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(filePath.toFile());
        ProgramInterfaceNotImplementation programInterfaceNotImplementation = new ProgramInterfaceNotImplementation(classNodeCreator, packagePath);
        List<Message> badImplementations = programInterfaceNotImplementation.run(classNode);
        assertEquals(0, badImplementations.size());
    }

    private void validate(Path classPath, List<String> expectedMessages){
        Path packagePath = Path.of("G:\\My Drive\\classes\\374SoftwareDesign\\Project\\project-202410-team02-202410\\target\\test-classes\\domain\\checks\\ProgramToInterfaceNotImplementationTestClasses");
        MyClassNode classNode = classNodeCreator.createMyClassNodeFromFile(classPath.toFile());
        ProgramInterfaceNotImplementation programInterfaceNotImplementation = new ProgramInterfaceNotImplementation(classNodeCreator, packagePath);
        List<Message> badImplementations = programInterfaceNotImplementation.run(classNode);
        for(int i = 0; i < badImplementations.size(); i ++){
            Message badImplementation = badImplementations.get(i);
            assertEquals(badImplementation.getCheckType(), CheckType.INTERFACE_OVER_IMPLEMENTATION);
            assertEquals(badImplementation.getClassOfInterest(), classNode.name);
            assertEquals(badImplementation.getMessage(), expectedMessages.get(i));
        }

    }


}
