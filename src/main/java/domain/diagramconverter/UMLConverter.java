package domain.diagramconverter;

import domain.MyClassNode;

public interface UMLConverter {
    public String convert(MyClassNode myclassNode, RelationsManager relationsManager);
}
