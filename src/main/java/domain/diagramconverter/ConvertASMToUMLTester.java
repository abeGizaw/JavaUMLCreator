package domain.diagramconverter;

import domain.MyClassNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConvertASMToUMLTester extends ConvertASMToUML {


    public ConvertASMToUMLTester(RelationsManager relationsManager, StringBuilder classUmlContent){
        super(relationsManager, classUmlContent);
    }

    public String TestGenerateDiagramByNode(MyClassNode myClassNode, StringBuilder pumlContent) {
        generateDiagramByNode(myClassNode, pumlContent);
        return super.classUmlContent.toString();
    }

    public String TestGenerateDiagramByPackage(Map<String, List<MyClassNode>> packageToMyClassNode) {
        return generateDiagramByPackage(packageToMyClassNode).toString();
    }

    public String getClassUMLContent() {
        return super.classUmlContent.toString();
    }


    public void generateDiagramByNode(MyClassNode myClassNode, StringBuilder pumlContent) {
        super.generateDiagramByNode(myClassNode, pumlContent);
    }

    public StringBuilder generateDiagramByPackage(Map<String, List<MyClassNode>> packageToMyClassNode) {
        return super.generateDiagramByPackage(packageToMyClassNode, "");

    }
}
