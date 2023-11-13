package domain.diagramconverter;

import domain.MyClassNode;

import java.util.List;

public interface Diagram {
    public void generateDiagramByNode(MyClassNode myClassNode, StringBuilder pumlContent);
    public StringBuilder generateDiagram(List<MyClassNode> myClassNodeList);
}
