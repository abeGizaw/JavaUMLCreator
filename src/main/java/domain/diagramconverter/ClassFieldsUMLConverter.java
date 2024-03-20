package domain.diagramconverter;

import domain.MyClassNode;
import domain.MyFieldNode;

import java.util.List;

public class ClassFieldsUMLConverter extends UMLConverterBase{

    @Override
    public String convert(MyClassNode myClassNode, RelationsManager relationsManager) {
        List<MyFieldNode> fields = myClassNode.fields;
        String className = myClassNode.name;

        StringBuilder fieldString = new StringBuilder();
        fieldString.append("{\n\t");
        for(MyFieldNode field: fields){
            appendFieldInfo(fieldString, field, className, relationsManager);
        }

        return fieldString.toString();
    }
}
