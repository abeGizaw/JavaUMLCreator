package domain;

import domain.myasm.MyASMClassNodeCreator;

import java.io.File;
import java.nio.file.Path;

public class MyClassNodeTesterCreator implements MyClassNodeCreator {

    private MyASMClassNodeCreator creator;

    public MyClassNodeTesterCreator(Path path) {
        this.creator = new MyASMClassNodeCreator(path);
    }

    private MyClassNodeTester convertClassNodeToTester(MyClassNode myClassNode) {
        return new MyClassNodeTester(myClassNode);
    }

    public MyClassNodeTester myClassNodeTesterFromName(String path){
        return convertClassNodeToTester(createMyClassNodeFromName(path));
    }

    public MyClassNodeTester myClassNodeTesterFromFile(File path){
        return convertClassNodeToTester(createMyClassNodeFromFile(path));
    }

    public MyClassNodeTester createUniqueMyClassNodeNodeFromName(String className) {
        return convertClassNodeToTester(createUniqueMyClassNodeNodeFromName(className));
    }



    @Override
    public MyClassNode createMyClassNodeFromName(String path) {
        return creator.createMyClassNodeFromName(path);
    }

    @Override
    public MyClassNode createMyClassNodeFromFile(File path) {
        return creator.createMyClassNodeFromFile(path);
    }

    @Override
    public MyClassNode createUniqueMyClassNodeFromName(String className) {
        return createUniqueMyClassNodeFromName(className);
    }
}
