package domain.checks;
import domain.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static presentation.ANSIColors.*;

public class ProgramInterfaceNotImplementation implements Check{
    private final MyClassNodeCreator classNodeCreator;
    private final Path basePath;

    public ProgramInterfaceNotImplementation(MyClassNodeCreator nodeCreator, Path startPath){
        classNodeCreator = nodeCreator;
        this.basePath = startPath;
    }

    public List<Message> run(MyClassNode myClassNode){
        return checkImplementInterface(myClassNode);
    }

    private List<Message> checkImplementInterface(MyClassNode classNode){
        List<Message> invalidUses = new ArrayList<>();

        for (MyFieldNode field : classNode.fields) {
            if(isPrimitive(field.desc)) continue;

            String className= getClassName(field.desc);

            if(isJavaAPIClass(className)){
                readJavaDefinedClass(classNode, className, field, invalidUses);
            } else {
                String relativePath = findRelativePath(className);
                readUserDefinedClass(classNode, relativePath, field, invalidUses);
            }

        }
        return invalidUses;
    }

    private String getClassName(String desc) {
        if(desc.startsWith("[")){
            return getClassName(desc.substring(1));
        }
        return  desc.substring(1, desc.length() - 1);
    }

    boolean isPrimitive(String desc) {
        if(desc.startsWith("[")){
            return isPrimitive(desc.substring(1));
        }
        return !desc.startsWith("L");
    }

    private boolean isJavaAPIClass(String className) {
        return className.startsWith("java/");
    }

    private String findRelativePath(String desc) {
        String filePackage = basePath.toString().substring(basePath.toString().lastIndexOf(File.separatorChar) + 1);
        String pathToFind = desc.replace('/', File.separatorChar);
        int separator = pathToFind.lastIndexOf(filePackage);
        if(separator == -1){
            //Class is not in given package
            return desc;
        }
        return pathToFind.substring(separator + filePackage.length());
    }

    private void readJavaDefinedClass(MyClassNode classNode, String classNamePath, MyFieldNode field, List<Message> invalidUses) {
        MyClassNode fieldClassNode = classNodeCreator.createMyClassNodeFromName(classNamePath);
        if(implementsInterfaceOrExtendsAbstractClass(fieldClassNode)) {
            String message = "Where you need to Programming to interface instead of implementation: " + field.name;
            invalidUses.add(new Message(CheckType.INTERFACE_OVER_IMPLEMENTATION, message, classNode.name));
        }
    }

    private void readUserDefinedClass(MyClassNode classNode, String relativePath, MyFieldNode field, List<Message> invalidUses) {
        Path classFilePath = basePath.resolve(basePath + relativePath +".class");

        MyClassNode fieldClassNode = classNodeCreator.createMyClassNodeFromFile(classFilePath.toFile());
        if (implementsInterfaceOrExtendsAbstractClass(fieldClassNode)) {
            String message = "Where you need to Programming to interface instead of implementation: " + field.name;
            invalidUses.add(new Message(CheckType.INTERFACE_OVER_IMPLEMENTATION, message, classNode.name));
        }

    }


    private boolean implementsInterfaceOrExtendsAbstractClass(MyClassNode fieldClassNode) {
        if ((fieldClassNode.access & MyOpcodes.ACC_FINAL) != 0) {
            return false;
        }

        if((fieldClassNode.access & MyOpcodes.ACC_INTERFACE) == 0 && (fieldClassNode.access & MyOpcodes.ACC_ABSTRACT) == 0){
            return !fieldClassNode.interfaces.isEmpty() || (fieldClassNode.superName != null && checkIfAbstract(fieldClassNode.superName));
        }
        return false;
    }

    private boolean checkIfAbstract(String superName) {
        if(isJavaAPIClass(superName)){
            MyClassNode myClassNode = classNodeCreator.createMyClassNodeFromName(superName);
            return (myClassNode.access & MyOpcodes.ACC_ABSTRACT) != 0;
        } else {
            Path classFilePath = basePath.resolve(basePath + findRelativePath(superName) + ".class");
            MyClassNode myClassNode = classNodeCreator.createMyClassNodeFromFile(classFilePath.toFile());
            return (myClassNode.access & MyOpcodes.ACC_ABSTRACT) != 0;
        }
    }
}
