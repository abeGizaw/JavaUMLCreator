package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KianasMain {
    public static void main(String[] args) throws IOException {
        List<String> classNames = new ArrayList<>();
        classNames.add("domain/ConcreteAdapter");
        classNames.add("domain/Adaptee");
        classNames.add("domain/Adapter");
        classNames.add("domain/ConcreteClass1");
        classNames.add("domain/Client");
        classNames.add("domain/ConcreteAdapter2");
        classNames.add("domain/Adapter2");

        List<ClassNode> classNodes = new ArrayList<>();
        for (String className : classNames) {
            ClassReader reader = new ClassReader(className);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            classNodes.add(classNode);
        }

        QDAdapterPatternCheck adapterPatternCheck = new QDAdapterPatternCheck(classNodes);
        adapterPatternCheck.run(classNodes.get(0));
    }
}
