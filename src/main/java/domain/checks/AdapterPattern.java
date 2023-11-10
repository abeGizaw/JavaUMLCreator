package domain.checks;


import domain.Message;
import domain.MyClassNode;
import domain.MyFieldNode;

import domain.*;


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
        List<AdapterPatternClasses> adapterPatterns = findAdapterPatterns();
        return createMessages(adapterPatterns);
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
                matchingAdapterPatterns.add(new AdapterPatternClasses(adapterPatternClasses.getAdapter(), adapterPatternClasses.getTarget(), adapterPatternClasses.getAdaptee(), myClassNode.name));
            }
        }
        return matchingAdapterPatterns;
    }

    private List<Message> createMessages(List<AdapterPatternClasses> adapterPatterns) {
        List<Message> messages = new ArrayList<>();
        for (AdapterPatternClasses adapterPattern : adapterPatterns) {
            String messageText = String.format("There is a possible use of the Adapter Pattern with\n" +
                    "\tadapter: %s\n" +
                    "\ttarget: %s\n" +
                    "\tadaptee: %s\n" +
                    "\tclient: %s.\n", adapterPattern.getAdapter(), adapterPattern.getTarget(), adapterPattern.getAdaptee(), adapterPattern.getClient());
            String classes = String.format("%s, %s, %s, %s", adapterPattern.getAdapter(), adapterPattern.getTarget(), adapterPattern.getAdaptee(), adapterPattern.getClient());
            Message message = new Message(LintType.ADAPTER_PATTERN, messageText, classes);
            messages.add(message);
        }
        return messages;
    }
}
