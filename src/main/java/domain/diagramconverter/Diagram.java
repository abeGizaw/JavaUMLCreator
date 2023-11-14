package domain.diagramconverter;

import domain.MyClassNode;

import java.util.List;
import java.util.Map;

public interface Diagram {
    public void generateDiagramByNode(MyClassNode myClassNode, StringBuilder pumlContent);
    public StringBuilder generateDiagramByPackage(List<MyClassNode> myClassNodeList, Map<String, List<MyClassNode>> packageToMyClassNode);
}
