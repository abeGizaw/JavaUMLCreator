package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class MyASMClassReader extends MyClassReader {
    private ClassReader classReader;
    private ClassNode classNode;

    @Override
    public MyClassNode generateMyClassNode(String className) throws IOException {
        this.classReader = new ClassReader(className);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        return convertThirdParty(classNode);
    }

    private MyClassNode convertThirdParty(ClassNode classNode) {
        MyClassNode myClassNode = new MyASMClassNode(classNode);
        return myClassNode;
    }
}
