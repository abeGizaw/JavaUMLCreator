package domain.diagramconverter;

import domain.MyClassNode;

public class ClassNameUMLConverter extends UMLConverterBase{
    @Override
    public String convert(MyClassNode myClassNode, RelationsManager relationsManager) {
        StringBuilder classString = new StringBuilder();
        String cleanClassName = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);

        String classType = getClassType(myClassNode.access);

        if(myClassNode.name.contains("$")){
            convertInnerClassInfo(myClassNode, classString, classType);
        } else {
            convertOuterClassInfo(myClassNode, classString, classType);
        }

        relationsManager.addExtendsRelationShip(myClassNode, cleanClassName);
        relationsManager.addImplementsRelationShip(myClassNode, cleanClassName);

        return classString.toString();
    }
}
