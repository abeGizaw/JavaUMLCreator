package domain.diagramconverter;

import domain.MyClassNodeTester;
import domain.MyClassNodeTesterCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertASMToUMLTest {

    @Test
    public void generateDiagramByNodeNullClassNode(){
        // Set Up
        RelationsManager relationsManager = new RelationsManager();

        ConvertASMToUMLTester convertASMToUMLTester = new ConvertASMToUMLTester(relationsManager, new StringBuilder());

        String expectedMessage = "Cannot read field \"access\" because \"myClassNode\" is null";

        // Execution
        Exception thrown = Assertions.assertThrows(NullPointerException.class,
                () -> convertASMToUMLTester.TestGenerateDiagramByNode(null, new StringBuilder()),
                "Cannot read field \"access\" because \"myClassNode\" is null");
        String actualMessage = thrown.getMessage();

        // Verification
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void generateDiagramByNodeNullPackage(){
        // Set Up
        RelationsManager relationsManager = new RelationsManager();

        ConvertASMToUMLTester convertASMToUMLTester = new ConvertASMToUMLTester(relationsManager, new StringBuilder());

        String expectedMessage = "Cannot invoke \"java.util.Map.keySet()\" because \"packageToMyClassNode\" is null";

        // Execution
        Exception thrown = Assertions.assertThrows(NullPointerException.class,
                () -> convertASMToUMLTester.TestGenerateDiagramByPackage(null),
                "Cannot read field \"access\" because \"myClassNode\" is null");
        String actualMessage = thrown.getMessage();

        // Verification
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void generateDiagramByFileEmptyString(){
        // Set Up
        RelationsManager relationsManager = new RelationsManager();

        ConvertASMToUMLTester convertASMToUMLTester = new ConvertASMToUMLTester(relationsManager, new StringBuilder());

        Path pathToTarget = Path.of(System.getProperty("user.dir") + File.separator + "target" + File.separator
                        + "test-classes" + File.separator + "domain" + File.separator + "diagramconverter"
                + File.separator + "ClassUmlMockTestClasses" + File.separator  + "AccessModifierVariety.class");

        String expected = "+class AccessModifierVariety{\n" +
                "\t+AccessModifierVariety():void\n" +
                "\t}\n";

        MyClassNodeTesterCreator testNodeCreator = new MyClassNodeTesterCreator(pathToTarget);
        MyClassNodeTester testNode = testNodeCreator.myClassNodeTesterFromFile(pathToTarget.toFile());
        ArrayList oneItem = new ArrayList();
        oneItem.add(testNode.methods.get(0));
        testNode.setMethods(oneItem);

        // Execution
        String result = convertASMToUMLTester.TestGenerateDiagramByNode(testNode, new StringBuilder());

        // Verification
        assertEquals(expected, result);
    }

    @Test
    public void generateDiagramByNodePartialString(){}

}
