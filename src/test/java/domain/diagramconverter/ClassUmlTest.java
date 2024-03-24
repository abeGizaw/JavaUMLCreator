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

    private long filesCompareByByte(Path path1, Path path2) throws IOException {
        try (BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(path1.toFile()));
             BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(path2.toFile()))) {

            int ch = 0;
            long pos = 1;
            while ((ch = fis1.read()) != -1) {
                if (ch != fis2.read()) {
                    return pos;
                }
                pos++;
            }
            if (fis2.read() == -1) {
                return -1;
            }
            else {
                return pos;
            }
        }
    }

    @Test
    public void compareHasATestFiles() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();

        Path pathOfTarget = Path.of("C:\\Users\\hueyee\\csse375\\JavaUMLCreator\\target\\test-classes\\domain\\diagramconverter\\ClassUmlMockTestClasses\\HasATest.class");
        Path pathOfCorrectPUML =  Path.of("C:\\Users\\hueyee\\csse375\\JavaUMLCreator\\HasACorrect\\test.puml");
        Path pathOfTestPUML =  Path.of("C:\\Users\\hueyee\\csse375\\JavaUMLCreator\\HasATest\\HasATest.puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, "HasATest", "HasATest");

        byte[] correctPUMLBytes = Files.readAllBytes(pathOfCorrectPUML);
        byte[] testPUMLBytes = Files.readAllBytes(pathOfTestPUML);

        //Validate
        assertArrayEquals(correctPUMLBytes, testPUMLBytes, "The files content should be the same");
    }

    @Test
    public void compareExtendsAndImplementsFiles() throws IOException {
        //Set Up
        LinterMain linterMain = new LinterMain();

        Path pathOfTarget = Path.of("C:\\Users\\hueyee\\csse375\\JavaUMLCreator\\target\\test-classes\\domain\\diagramconverter\\ClassUmlMockTestClasses\\ExtendsAndImplements");
        Path pathOfCorrectPUML =  Path.of("C:\\Users\\hueyee\\csse375\\JavaUMLCreator\\ExtendsAndImplementsCorrect\\ExtendsAndImplementsCorrect.puml");
        Path pathOfTestPUML =  Path.of("C:\\Users\\hueyee\\csse375\\JavaUMLCreator\\ExtendsAndImplementsTest\\ExtendsAndImplementsTest.puml");

        //Execute Test
        linterMain.generateUMLFromPath(pathOfTarget, "ExtendsAndImplementsTest", "ExtendsAndImplementsTest");

        byte[] correctPUMLBytes = Files.readAllBytes(pathOfCorrectPUML);
        byte[] testPUMLBytes = Files.readAllBytes(pathOfTestPUML);

        //Validate
        assertArrayEquals(correctPUMLBytes, testPUMLBytes, "The files content should be the same");
    }



}
