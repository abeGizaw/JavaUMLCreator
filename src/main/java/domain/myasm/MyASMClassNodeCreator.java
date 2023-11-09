package domain.myasm;

import domain.MyClassNode;
import domain.MyClassNodeCreator;
import domain.myasm.MyASMClassNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyASMClassNodeCreator implements MyClassNodeCreator {
    public MyClassNode createMyClassNodeFromName(String path) {
        ClassNode classNode;
        try {
            ClassReader reader = new ClassReader(path);
            classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            throw new RuntimeException("This is not a valid class name: " + path);
        }

        return new MyASMClassNode(classNode);
    }

    public MyClassNode createMyClassNodeFromFile(File path) {
        ClassNode classNode;
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            ClassReader reader = new ClassReader(fileInputStream);
            classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            throw new RuntimeException("The class file for " + path + " cannot be found. Please provide the correct directory.");
        }

        return new MyASMClassNode(classNode);
    }
}