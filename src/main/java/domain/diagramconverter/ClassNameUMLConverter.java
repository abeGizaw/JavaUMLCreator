package domain.diagramconverter;

import domain.MyClassNode;
import domain.MyInnerClassNode;
import domain.MyOpcodes;

import java.util.List;
import java.util.stream.Collectors;

public class ClassNameUMLConverter extends UMLConverterBase{
    private ClassType classType;
    @Override
    public String convert(MyClassNode myClassNode, RelationsManager relationsManager) {
        StringBuilder classString = new StringBuilder();
        this.classType = ClassType.getClassType(myClassNode.access);

        handleRelations(relationsManager, myClassNode);

        if(myClassNode.name.contains("$")){
            convertInnerClassInfo(myClassNode, classString);
        } else {
            convertOuterClassInfo(myClassNode, classString);
        }

        return classString.toString();
    }

    private void handleRelations(RelationsManager relationsManager, MyClassNode myClassNode) {
        String cleanClassName = cleanClassName(myClassNode.name);
        this.classType = relationsManager.addExtendsRelationShip(myClassNode, cleanClassName, this.classType);

        relationsManager.addImplementsRelationShip(myClassNode, cleanClassName);
        if(classType != ClassType.ANNOTATION){
            List<String> annotationNames = myClassNode.annotations.stream()
                    .map(ann -> cleanClassName(ann.desc))
                    .collect(Collectors.toList());
            relationsManager.addAnnotationRelationship(annotationNames, cleanClassName);
        }
    }


    /**
     * Determines the UML type based on the access flags.
     *
     * @param access Access flags of the class.
     * @return A string representing the UML type.
     */
    private String getClassType(int access) {
        if((access & MyOpcodes.ACC_ANNOTATION) != 0){
            return "annotation";
        } else if((access & MyOpcodes.ACC_ABSTRACT) != 0){
            return "abstract class";
        } else if((access & MyOpcodes.ACC_ENUM) != 0){
            return "enum";
        } else if((access & MyOpcodes.ACC_INTERFACE) != 0){
            return "interface";
        }else {
            return "class";
        }
    }


    /**
     * Generates UML information for non-inner (outer) classes.
     *
     * @param myClassNode MyClassNode representing the class.
     * @param classString StringBuilder to append the class information.
     */
    private void convertOuterClassInfo(MyClassNode myClassNode, StringBuilder classString) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);
        if (classType == ClassType.ENUM) {
            classString.append(classType).append(" ").append(className);
        } else {
            String classModifier = getAccessModifier(myClassNode.access);
            classString.append(classModifier).append(classType.getDescription()).append(" ").append(className);

            if(classType == ClassType.RECORD){
                classString.append(" <<record>>");
            }
        }
    }


    /**
     * Generates UML information for inner classes.
     *
     * @param myClassNode MyClassNode representing the class.
     * @param classString StringBuilder to append the class information.
     */
    private void convertInnerClassInfo(MyClassNode myClassNode, StringBuilder classString) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("$") + 1);
        MyInnerClassNode innerClassNode = findInnerClassNode(myClassNode, myClassNode.name);
        if(innerClassNode != null){
            String classModifier = getAccessModifier(innerClassNode.access);
            classString.append(classModifier).append(classType.getDescription()).append(" ").append(className);
        }
    }


    private MyInnerClassNode findInnerClassNode(MyClassNode myClassNode, String name) {
        for (MyInnerClassNode icn : myClassNode.innerClasses) {
            if (icn.name.equals(name)) {
                return icn;
            }
        }
        return null;
    }
}
