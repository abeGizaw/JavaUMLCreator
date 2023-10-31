package domain.kianascode;

import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QDAdapterPatternCheck {
    List<ClassNode> classNodes;
    private List<String> classNames;
    private Set<String> interfaceNames;

    public QDAdapterPatternCheck(List<ClassNode> classNodes) {
        this.classNodes = classNodes;

        classNames = new ArrayList<>();
        for (ClassNode classNode : classNodes) {
            classNames.add(classNode.name);
        }

        interfaceNames = new HashSet<>();
    }

    public void run(ClassNode classNode) {
        printAdapterPatterns(findAdapterPatterns());
    }

    private List<AdapterPatternClasses> findAdapterPatterns() {
        List<AdapterPatternClasses> possibleAdapterPatterns = new ArrayList<>();
        for (ClassNode classNode : classNodes) {
            List<String> implementedInterfaces = getImplementedInterfaces(classNode);
            for (String interfaceName : implementedInterfaces) {
                interfaceNames.add(interfaceName);
                for (String fieldType : getNotJavaClassFieldTypes(classNode)) {
                    possibleAdapterPatterns.add(new AdapterPatternClasses(classNode.name, interfaceName, fieldType));
                }
            }
        }

        return findAdapterPatternClients(possibleAdapterPatterns);
    }

    private List<String> getImplementedInterfaces(ClassNode classNode) {
        List<String> implementedInterfaces = new ArrayList<>();
        implementedInterfaces.addAll(classNode.interfaces);
        String[] parts = classNode.superName.split("/");
        if (!parts[0].isEmpty() && !parts[0].equals("java")) {
            implementedInterfaces.add(classNode.superName);
        }
        return implementedInterfaces;
    }

    private List<String> getNotJavaClassFieldTypes(ClassNode classNode) {
        List<String> fieldTypes = new ArrayList<>();
        for (FieldNode fieldNode : classNode.fields) {
            String fieldType = fieldNode.desc.substring(1,fieldNode.desc.length() - 1);
            if (this.fieldTypeIsClass(fieldType)) {
                fieldTypes.add(fieldType);
            }
        }
        return fieldTypes;
    }

    private boolean fieldTypeIsClass(String fieldType) {
        for (String className : classNames) {
            if (className.equals(fieldType)) {
                return true;
            }
        }
        return false;
    }

    private List<AdapterPatternClasses> findAdapterPatternClients(List<AdapterPatternClasses> possibleAdapterPatterns) {
        List<AdapterPatternClasses> adapterPatterns = new ArrayList<>();
        for (ClassNode classNode : classNodes) {
            for (FieldNode fieldNode : classNode.fields) {
                String fieldType = fieldNode.desc.substring(1,fieldNode.desc.length() - 1);
                for (String interfaceName : this.findImplementedInterfaces(fieldType)) {
                    adapterPatterns.addAll(getAdapterPatternClassesForInterface(interfaceName, possibleAdapterPatterns, classNode));
                }
            }
        }
        return adapterPatterns;
    }

    private List<String> findImplementedInterfaces(String className) {
        List<String> implementedInterfaces = new ArrayList<>();
        for (String interfaceName : interfaceNames) {
            if (interfaceName.equals(className)) {
                implementedInterfaces.add(interfaceName);
            }
        }
        return implementedInterfaces;
    }

    private List<AdapterPatternClasses> getAdapterPatternClassesForInterface(String interfaceName, List<AdapterPatternClasses> possibleAdapterPatterns, ClassNode classNode) {
        List<AdapterPatternClasses> matchingAdapterPatterns = new ArrayList<>();
        for (AdapterPatternClasses adapterPatternClasses : possibleAdapterPatterns) {
            if (adapterPatternClasses.adapter.equals(interfaceName)) {
                matchingAdapterPatterns.add(new AdapterPatternClasses(adapterPatternClasses.adapter, adapterPatternClasses.concreteAdapter, adapterPatternClasses.adaptee, classNode.name));
            }
        }
        return matchingAdapterPatterns;
    }

    private void printAdapterPatterns(List<AdapterPatternClasses> adapterPatterns) {
        for (AdapterPatternClasses adapterPatternClasses : adapterPatterns) {
            System.out.printf("There is a possible use of the Adapter Pattern with\n" +
                    "\tadapter: %s\n" +
                    "\tconcreteAdapter: %s\n" +
                    "\tadaptee: %s\n" +
                    "\tclient: %s.\n", adapterPatternClasses.adapter, adapterPatternClasses.concreteAdapter, adapterPatternClasses.adaptee, adapterPatternClasses.client);
        }
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
