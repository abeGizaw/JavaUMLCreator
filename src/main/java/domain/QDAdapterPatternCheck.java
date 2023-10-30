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
        classNames.add("domain/Client");
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
        List<AdapterPatternClasses> possibleAdapterPatterns = new ArrayList<>();
        List<String> interfaces = new ArrayList<>();
        for (ClassNode classNode : classNodes) {
            for (String interfaceName : classNode.interfaces) {
                interfaces.add(interfaceName);
                for (FieldNode fieldNode : classNode.fields) {
                    String fieldType = fieldNode.desc.substring(1,fieldNode.desc.length() - 1);
                    if (this.fieldTypeIsClass(fieldType)) {
                        possibleAdapterPatterns.add(new AdapterPatternClasses(classNode.name, interfaceName, fieldType));
                    }
                }
            }
        }

        List<AdapterPatternClasses> adapterPatterns = new ArrayList<>();

        for (ClassNode classNode : classNodes) {
            for (FieldNode fieldNode : classNode.fields) {
                String fieldType = fieldNode.desc.substring(1,fieldNode.desc.length() - 1);
                for (String interfaceName : this.findImplementedInterfaces(fieldType, interfaces)) {
                    for (AdapterPatternClasses adapterPatternClasses : possibleAdapterPatterns) {
                        if (adapterPatternClasses.adapter.equals(interfaceName)) {
                            adapterPatterns.add(new AdapterPatternClasses(adapterPatternClasses.adapter, adapterPatternClasses.concreteAdapter, adapterPatternClasses.adaptee, classNode.name));
                        }
                    }
                }
            }
        }

        for (AdapterPatternClasses adapterPatternClasses : adapterPatterns) {
            System.out.printf("There is a possible use of the Adapter Pattern with\n" +
                    "\tadapter: %s\n" +
                    "\tconcreteAdapter: %s\n" +
                    "\tadaptee: %s\n" +
                    "\tclient: %s.\n", adapterPatternClasses.adapter, adapterPatternClasses.concreteAdapter, adapterPatternClasses.adaptee, adapterPatternClasses.client);
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

    private List<String> findImplementedInterfaces(String className, List<String> interfaces) {
        List<String> implementedInterfaces = new ArrayList<>();
        for (String interfaceName : interfaces) {
            if (interfaceName.equals(className)) {
                implementedInterfaces.add(interfaceName);
            }
        }
        return implementedInterfaces;
    }

    private class AdapterPatternClasses {
        private String adapter;
        private String concreteAdapter;
        private String adaptee;
        private String client;

        private AdapterPatternClasses(String concreteAdapter, String adapter, String adaptee, String client) {
            this.concreteAdapter = concreteAdapter;
            this.adapter = adapter;
            this.adaptee = adaptee;
            this.client = client;
        }

        private AdapterPatternClasses(String concreteAdapter, String adapter, String adaptee) {
            this(concreteAdapter, adapter, adaptee, null);
        }
    }
}
