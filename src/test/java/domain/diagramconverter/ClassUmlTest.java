package domain.diagramconverter;

import org.junit.jupiter.api.Test;
import presentation.LinterMain;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassUmlTest {

    private String testLocation = "TestPUMLS";
    private String correctLocation = "CorrectPUMLs";


    @Test
    public void testHasATestFiles() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "HasATest";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") + File.separator + "target" + File.separator
                + "test-classes" + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + "HasATest.class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + "HasACorrect" + File.separator + "test.puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testExtendsAndImplementsFiles() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();

        Path pathOfTarget = Path.of(System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + "ExtendsAndImplements");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") + File.separator + "CorrectPUMLs" + File.separator + "ExtendsAndImplementsCorrect" + File.separator + "ExtendsAndImplementsCorrect.puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") + File.separator + "TestPUMLS" + File.separator + "ExtendsAndImplementsTest" + File.separator + "ExtendsAndImplementsTest.puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, "TestPUMLS" + File.separator + "ExtendsAndImplementsTest", "ExtendsAndImplementsTest");

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testAccessModifierVariety() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "AccessModifierVariety";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testArrayFieldsConverter() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "ArrayFieldsConverter";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testCollectionFieldsConverter() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "CollectionFieldsConverter";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testImplementingClass() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "ImplementingClass";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testMockAbstract() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "MockAbstract";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testMockEnum() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "MockEnum";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testMockInterface() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "MockInterface";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testObjectFieldsConverter() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "ObjectFieldsConverter";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testParameterTypeVariety() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "ParameterTypeVariety";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testPrimitiveFieldsConverter() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "PrimitiveFieldsConverter";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }

    @Test
    public void testReturnTypeVariety() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();


        String testPathString = "ReturnTypeVariety";

        Path pathOfTarget = Path.of(System.getProperty("user.dir") +
                File.separator + "target" + File.separator + "test-classes"
                + File.separator + "domain" + File.separator + "diagramconverter" + File.separator + "ClassUmlMockTestClasses" + File.separator + testPathString + ".class");
        Path pathOfCorrectPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + correctLocation + File.separator + testPathString + File.separator + testPathString + ".puml");
        Path pathOfTestPUML =  Path.of(System.getProperty("user.dir") +
                File.separator + testLocation + File.separator + testPathString + File.separator + testPathString + ".puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, testLocation + File.separator + testPathString, testPathString);

        List<String> correctPUML = Files.readAllLines(pathOfCorrectPUML);
        List<String> testPUML = Files.readAllLines(pathOfTestPUML);

        //Validate
        assertEquals(correctPUML.size(), testPUML.size(), "Files should have the same number of lines");

        for (int i = 0; i < correctPUML.size(); i++) {
            assertEquals(correctPUML.get(i), testPUML.get(i), "Mismatch at line " + (i + 1));
        }
    }





}
