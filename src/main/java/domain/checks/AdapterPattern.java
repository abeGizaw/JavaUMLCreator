package domain.checks;

import domain.Message;
import domain.MyClassNode;
import domain.MyFieldNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdapterPattern implements Check {
    private List<MyClassNode> myClassNodes;
    private List<String> classNames;
    private Set<String> interfaceNames;

    public AdapterPattern(List<MyClassNode> myClassNodes) {
        this.myClassNodes = myClassNodes;

        classNames = new ArrayList<>();
        for (MyClassNode myClassNode : myClassNodes) {
            classNames.add(myClassNode.name);
        }

        interfaceNames = new HashSet<>();
    }

    public List<Message> run(MyClassNode myClassNode) {
        printAdapterPatterns(findAdapterPatterns());
        return null;
    }

    private List<AdapterPatternClasses> findAdapterPatterns() {
        List<AdapterPatternClasses> possibleAdapterPatterns = new ArrayList<>();
        for (MyClassNode myClassNode : myClassNodes) {
            List<String> implementedInterfaces = getImplementedInterfaces(myClassNode);
            for (String interfaceName : implementedInterfaces) {
                interfaceNames.add(interfaceName);
                for (String fieldType : getNotJavaClassFieldTypes(myClassNode)) {
                    possibleAdapterPatterns.add(new AdapterPatternClasses(myClassNode.name, interfaceName, fieldType));
                }
            }
        }

        return findAdapterPatternClients(possibleAdapterPatterns);
    }

    private List<String> getImplementedInterfaces(MyClassNode myClassNode) {
        List<String> implementedInterfaces = new ArrayList<>();
        implementedInterfaces.addAll(myClassNode.interfaces);
        String[] parts = myClassNode.superName.split("/");
        if (!parts[0].isEmpty() && !parts[0].equals("java")) {
            implementedInterfaces.add(myClassNode.superName);
        }
        return implementedInterfaces;
    }

    private List<String> getNotJavaClassFieldTypes(MyClassNode myClassNode) {
        List<String> fieldTypes = new ArrayList<>();
        for (MyFieldNode myFieldNode : myClassNode.fields) {
            String fieldType = myFieldNode.desc.substring(1,myFieldNode.desc.length() - 1);
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
        for (MyClassNode myClassNode : myClassNodes) {
            for (MyFieldNode myFieldNode : myClassNode.fields) {
                String fieldType = myFieldNode.desc.substring(1,myFieldNode.desc.length() - 1);
                for (String interfaceName : this.findImplementedInterfaces(fieldType)) {
                    adapterPatterns.addAll(getAdapterPatternClassesForInterface(interfaceName, possibleAdapterPatterns, myClassNode));
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

    private List<AdapterPatternClasses> getAdapterPatternClassesForInterface(String interfaceName, List<AdapterPatternClasses> possibleAdapterPatterns, MyClassNode myClassNode) {
        List<AdapterPatternClasses> matchingAdapterPatterns = new ArrayList<>();
        for (AdapterPatternClasses adapterPatternClasses : possibleAdapterPatterns) {
            if (adapterPatternClasses.getTarget().equals(interfaceName)) {
                matchingAdapterPatterns.add(new AdapterPatternClasses(adapterPatternClasses.getTarget(), adapterPatternClasses.getAdapter(), adapterPatternClasses.getAdaptee(), myClassNode.name));
            }
        }
        return matchingAdapterPatterns;
    }

    private void printAdapterPatterns(List<AdapterPatternClasses> adapterPatterns) {
        for (AdapterPatternClasses adapterPatternClasses : adapterPatterns) {
            System.out.printf("There is a possible use of the Adapter Pattern with\n" +
                    "\tadapter: %s\n" +
                    "\ttarget: %s\n" +
                    "\tadaptee: %s\n" +
                    "\tclient: %s.\n", adapterPatternClasses.getAdaptee(), adapterPatternClasses.getTarget(), adapterPatternClasses.getAdaptee(), adapterPatternClasses.getClient());
        }
    }
}
