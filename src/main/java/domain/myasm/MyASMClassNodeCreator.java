package domain.myasm;

import domain.MyClassNode;
import domain.MyClassNodeCreator;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class MyASMClassNodeCreator implements MyClassNodeCreator {
    private final Path directoryPath;
    private final int PACKAGE_NOT_FOUND = -1;
    public MyASMClassNodeCreator(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    private MyClassNode createMyClassNode(ClassReader reader) {
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        return new MyASMClassNode(classNode);
    }

    public MyClassNode createMyClassNodeFromName(String path) {
        try {
            ClassReader reader = new ClassReader(path);
            return createMyClassNode(reader);
        } catch (IOException e) {
            throw new RuntimeException("This is not a valid class name: " + path);
        }
    }

    public MyClassNode createMyClassNodeFromFile(File path) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            ClassReader reader = new ClassReader(fileInputStream);
            return createMyClassNode(reader);
        } catch (IOException e) {
            throw new RuntimeException("The class file for " + path + " cannot be found.");
        }
    }

    public MyClassNode createUniqueMyClassNodeFromName(String className) {
        String relativePath = findRelativePath(className);
        Path classFilePath = directoryPath.resolve(directoryPath + relativePath + ".class");
        try (FileInputStream fileInputStream = new FileInputStream(classFilePath.toFile())) {
            ClassReader reader = new ClassReader(fileInputStream);
            return createMyClassNode(reader);
        } catch (IOException e) {
            throw new RuntimeException("The class file for " + classFilePath + " cannot be found.\nProvide the correct directory with .class file for " + className);
        }
    }


    private String findRelativePath(String desc) {
        String filePackage = directoryPath.toString().substring(directoryPath.toString().lastIndexOf(File.separatorChar) + 1);
        String pathToFind = desc.replace('/', File.separatorChar);
        int separator = pathToFind.lastIndexOf(filePackage);
        if(separator == PACKAGE_NOT_FOUND){
            return File.separatorChar + desc;
        }
        return pathToFind.substring(separator + filePackage.length());
    }
}