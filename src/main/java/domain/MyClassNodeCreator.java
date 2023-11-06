package domain;

import domain.myasm.MyASMClassNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassNodeCreator {

    public MyClassNode crateMyClassNodeFromName(String path){

        ClassReader reader = null; // this will need to change to use a MyClassReader and a MyClassNode
        ClassNode classNode = null;
        try {
            reader = new ClassReader(path);
            classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            throw new RuntimeException("This is not a valid class name: " + path);
        }

       return new MyASMClassNode(classNode);
    }

    public MyClassNode crateMyClassNodeFromFile(File path){

        ClassReader reader = null; // this will need to change to use a MyClassReader and a MyClassNode
        ClassNode classNode = null;
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            reader = new ClassReader(fileInputStream);
            classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            throw new RuntimeException("This is not a valid file path " + path);
        }

        return new MyASMClassNode(classNode);
    }




}
