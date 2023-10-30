package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QDAdapterPatternCheck {
    private List<ClassNode> implementsInterfaceHasClass;
    private List<String> classNames;

    public QDAdapterPatternCheck(List<ClassNode> classNodes) {
        implementsInterfaceHasClass = new ArrayList<>();

        classNames = new ArrayList<>();
        for (ClassNode classNode : classNodes) {
            classNames.add(classNode.name);
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> classNames = new ArrayList<>();
        classNames.add("domain/ConcreteAdapter");
        classNames.add("domain/Adaptee");
        classNames.add("domain/Adapter");
        classNames.add("domain/ConcreteClass1");
        // TODO: how to pass in all classes in a package or directory?

        List<ClassNode> classNodes = new ArrayList<>();
        for (String className : classNames) {
            ClassReader reader = new ClassReader(className);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            classNodes.add(classNode);
        }

        QDAdapterPatternCheck qdAdapterPatternCheck = new QDAdapterPatternCheck(classNodes);
        qdAdapterPatternCheck.checkForAdapterPattern(classNodes);
    }

    private void checkForAdapterPattern(List<ClassNode> classNodes) {
        for (ClassNode classNode : classNodes) {
            if (!classNode.interfaces.isEmpty()) {
                for (FieldNode fieldNode : classNode.fields) {
                    String fieldType = fieldNode.desc.substring(1,fieldNode.desc.length() - 1);
                    if (this.fieldTypeIsClass(fieldType)) {
                        for (String interfaceName : classNode.interfaces) {
                            System.out.printf("possible concrete adapter: %s, possible adapter: %s, possible adaptee: %s\n", classNode.name, interfaceName, fieldType);
                        }
                    }
                }
            }
        }
    }

    private boolean fieldTypeIsClass(String fieldType) {
        for (String className : classNames) {
            if (className.equals(fieldType)) {
                return true;
            }
        }
        return false;
    }
}
