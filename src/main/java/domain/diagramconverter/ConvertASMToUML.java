package domain.diagramconverter;
import domain.*;
import presentation.ANSIColors;

import java.util.*;

/**
 * Converts ASM representation of classes to UML diagrams.
 * This class provides methods to generate UML diagram content from a given set of class nodes,
 * handling the conversion of class information, fields, and methods into UML syntax.
 */
public class ConvertASMToUML implements Diagram{

    private final RelationsManager relationManager;
    private final StringBuilder classUmlContent;
    private final List<UMLConverter> converters = new ArrayList<>();


    public ConvertASMToUML(RelationsManager relationsManager, StringBuilder classUmlContent){
        this.relationManager = relationsManager;
        this.classUmlContent = classUmlContent;
        addConverters();
    }


    private void addConverters() {
        converters.add(new ClassNameUMLConverter());
        converters.add(new ClassFieldsUMLConverter());
        converters.add(new ClassMethodsUMLConverter());
    }

    /**
     * Generates UML diagram content for a single class node and appends it to the provided StringBuilder.
     * This includes the class's fields, methods, and any relationships identified during conversion.
     *
     * @param myClassNode  The class node to convert into UML diagram content.
     * @param pumlContent  The StringBuilder instance to which the UML content will be appended.
     */
    public void generateDiagramByNode(MyClassNode myClassNode, StringBuilder pumlContent) {
        for(UMLConverter converter: converters){
            classUmlContent.append(converter.convert(myClassNode, this.relationManager));
        }

    }

    /**
     * Generates a UML diagram for an entire package, organizing class nodes by package and converting each to UML content.
     * This method also handles the aggregation of relationship information across classes.
     *
     * @param packageToMyClassNode  A map associating package names with lists of MyClassNode instances belonging to each package.
     * @return                      A StringBuilder containing the complete UML diagram content.
     */
    public StringBuilder generateDiagramByPackage(Map<String, List<MyClassNode>> packageToMyClassNode, String jsonPackage) {
        classUmlContent.append("@startuml\n");
        for(String packageName : packageToMyClassNode.keySet()){
            if (!packageName.isEmpty()) {
                classUmlContent.append("package ").append(packageName).append(" {\n\t");
            }

            List<MyClassNode> nodesForPackage = packageToMyClassNode.get(packageName);
            for (MyClassNode myClassNode : nodesForPackage) {
                generateDiagramByNode(myClassNode, classUmlContent);
                classUmlContent.append("\n");
            }

            if (!packageName.isEmpty()) {
                classUmlContent.append("}\n");
            }
        }

        if(!jsonPackage.isEmpty()){
            JsonFilesReader jsonFilesReader = new JsonFilesReader(jsonPackage);
            List<String> lines = jsonFilesReader.readJsonFilesInDirectory();
            jsonFilesReader.parse(lines);
            jsonFilesReader.convertJsonToUML(classUmlContent);
        }

        classUmlContent.append(this.relationManager.addAllRelations());

        classUmlContent.append("@enduml");
        return classUmlContent;

    }


}
