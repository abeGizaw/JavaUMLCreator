package domain;

import domain.diagramconverter.ConvertASMToUML;
import domain.diagramconverter.Diagram;

import java.nio.file.Path;
import java.util.*;

public class Linter {
    private final MyClassNodeCreator creator;
    private final List<MyClassNode> myClassNodes;
    private final Map<LintType, Diagram> diagramTypeToDiagram;
    private final Map<String, List<MyClassNode>> packageToMyClassNode = new HashMap<>();

    public Linter(List<String> classPaths, MyClassNodeCreator myClassNodeCreator, String outputPath, Map<String, String> fileToPackage) {
        this.creator = myClassNodeCreator;
        this.myClassNodes = createClassNodes(classPaths, fileToPackage);
        this.diagramTypeToDiagram = new HashMap<>();
        populateMaps(outputPath);
    }

    private void populateMaps(String outputPath) {
        populateDiagramMap();
    }
    private void populateDiagramMap() {
        diagramTypeToDiagram.put(LintType.UML_CONVERTER, new ConvertASMToUML(new StringBuilder()));

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



    public Map<StringBuilder, LintType> generateDiagrams(Set<LintType> diagrams) {
        Map<StringBuilder, LintType> diagramBuilders = new HashMap<>();
        for(LintType lintType: diagrams){
            Diagram diagram = diagramTypeToDiagram.get(lintType);
            StringBuilder diagramBuilder = diagram.generateDiagramByPackage(myClassNodes, packageToMyClassNode);
            diagramBuilders.put(diagramBuilder, lintType);

        }
        return diagramBuilders;
    }
}
