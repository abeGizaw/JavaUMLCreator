package domain;

import domain.diagramconverter.ConvertASMToUML;
import domain.diagramconverter.Diagram;

import java.nio.file.Path;
import java.util.*;

public class Linter {
    private final MyClassNodeCreator creator;
    private final List<MyClassNode> myClassNodes;
    private final Map<DiagramType, Diagram> diagramTypeToDiagram;
    private final Map<String, List<MyClassNode>> packageToMyClassNode = new HashMap<>();

    public Linter(List<String> classPaths, MyClassNodeCreator myClassNodeCreator, String outputPath, Map<String, String> fileToPackage) {
        this.creator = myClassNodeCreator;
        this.myClassNodes = createClassNodes(classPaths, fileToPackage);
        this.diagramTypeToDiagram = new HashMap<>();
        populateMaps();
    }

    private void populateMaps() {
        populateDiagramMap();
    }
    private void populateDiagramMap() {
        diagramTypeToDiagram.put(DiagramType.UML_CONVERTER, new ConvertASMToUML(new StringBuilder()));
    }

    private List<MyClassNode> createClassNodes(List<String> classPaths, Map<String, String> fileToPackage) {
        List<MyClassNode> myNodes = new ArrayList<>();
        for (String path : classPaths) {
            String packageName = fileToPackage.get(path);
            packageToMyClassNode.putIfAbsent(packageName, new ArrayList<>());
            Path p = Path.of(path);
            MyClassNode myClassNode = creator.createMyClassNodeFromFile(p.toFile());
            myNodes.add(myClassNode);
            packageToMyClassNode.get(packageName).add(myClassNode);
        }
        return myNodes;
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
