package domain.diagramconverter;
import domain.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts ASM representation of classes to UML diagrams.
 * This class provides methods to generate UML diagram content from a given set of class nodes,
 * handling the conversion of class information, fields, and methods into UML syntax.
 */
public class ConvertASMToUMLMock implements Diagram{

    private final RelationsManager relationManager;
    private final StringBuilder classUmlContent;
    private final List<UMLConverter> converters = new ArrayList<>();


    public ConvertASMToUMLMock(RelationsManager relationsManager, StringBuilder classUmlContent){
        this.relationManager = relationsManager;
        this.classUmlContent = classUmlContent;
        addConverters();
    }

    private void addConverters() {
        converters.add(new ClassFieldsUMLConverter());
        converters.add(new ClassMethodsUMLConverter());
        converters.add(new ClassNameUMLConverter());
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
            converter.convert(myClassNode, this.relationManager);
        }
//        pumlContent.append(convertClassInfo(myClassNode));
//        pumlContent.append("{\n\t");
//
//        String cleanClassName = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);
//
//        pumlContent.append(convertClassFields(myClassNode.fields, myClassNode.name));
//        pumlContent.append(convertClassMethods(myClassNode.methods, cleanClassName));
//
//        pumlContent.append("}\n");


//        allHasARelationships.addAll(convertKeyNames(hasARelationShipByClass));
//        addExtendsAndImplementsRelation(myClassNode, cleanClassName);
//        hasARelationShipByClass.clear();
    }

    /**
     * Generates a UML diagram for an entire package, organizing class nodes by package and converting each to UML content.
     * This method also handles the aggregation of relationship information across classes.
     *
     * @param packageToMyClassNode  A map associating package names with lists of MyClassNode instances belonging to each package.
     * @return                      A StringBuilder containing the complete UML diagram content.
     */
    public StringBuilder generateDiagramByPackage(Map<String, List<MyClassNode>> packageToMyClassNode) {
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
//        for(String relationship: allHasARelationships){
//            classUmlContent.append(relationship).append("\n");
//        }
        classUmlContent.append("@enduml");
        return classUmlContent;
    }



}
