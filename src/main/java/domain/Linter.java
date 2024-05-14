package domain;

import domain.diagramconverter.ConvertASMToUML;
import domain.diagramconverter.Diagram;
import domain.diagramconverter.RelationsManager;

import java.nio.file.Path;
import java.util.*;

public class Linter {
    private final MyClassNodeCreator creator;
    private final Map<DiagramType, Diagram> diagramTypeToDiagram;
    private final Map<String, List<MyClassNode>> packageToMyClassNode = new HashMap<>();

    public Linter(List<String> classPaths, MyClassNodeCreator myClassNodeCreator, Map<String, String> fileToPackage) {
        this.creator = myClassNodeCreator;
        createClassNodes(classPaths, fileToPackage);
        this.diagramTypeToDiagram = new HashMap<>();
        populateDiagramMap();
    }

    private void populateDiagramMap() {
        diagramTypeToDiagram.put(DiagramType.UML_CONVERTER, new ConvertASMToUML(new RelationsManager(), new StringBuilder()));
    }

    private void createClassNodes(List<String> classPaths, Map<String, String> fileToPackage) {
        for (String path : classPaths) {
            String packageName = fileToPackage.get(path);
            packageToMyClassNode.putIfAbsent(packageName, new ArrayList<>());
            Path p = Path.of(path);
            MyClassNode myClassNode = creator.createMyClassNodeFromFile(p.toFile());
            packageToMyClassNode.get(packageName).add(myClassNode);
        }
    }

    public Map<StringBuilder, DiagramType> generateDiagrams(Set<DiagramType> diagrams) {
        Map<StringBuilder, DiagramType> diagramBuilders = new HashMap<>();
        for(DiagramType diagramType : diagrams){
            Diagram diagram = diagramTypeToDiagram.get(diagramType);
            StringBuilder diagramBuilder = diagram.generateDiagramByPackage(packageToMyClassNode);
            diagramBuilders.put(diagramBuilder, diagramType);

        }
        return diagramBuilders;
    }
}
