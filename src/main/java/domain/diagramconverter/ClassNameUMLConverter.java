package domain.diagramconverter;

import domain.MyClassNode;
import domain.MyInnerClassNode;
import domain.MyOpcodes;

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


    /**
     * Determines the UML type based on the access flags.
     *
     * @param access Access flags of the class.
     * @return A string representing the UML type.
     */
    private String getClassType(int access) {
        if((access & MyOpcodes.ACC_INTERFACE) != 0){
            return "interface";
        } else if((access & MyOpcodes.ACC_ABSTRACT) != 0){
            return "abstract class";
        } else if((access & MyOpcodes.ACC_ENUM) != 0){
            return "enum";
        } else {
            return "class";
        }
    }


    /**
     * Generates UML information for non-inner (outer) classes.
     *
     * @param myClassNode MyClassNode representing the class.
     * @param classString StringBuilder to append the class information.
     * @param classType The type of class (e.g., class, abstract class, interface).
     */
    private void convertOuterClassInfo(MyClassNode myClassNode, StringBuilder classString, String classType) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("/") + 1);
        if (classType.equals("enum")) {
            classString.append(classType).append(" ").append(className);
        } else {
            String classModifier = getAccessModifier(myClassNode.access);
            classString.append(classModifier).append(classType).append(" ").append(className);
        }
    }


    /**
     * Generates UML information for inner classes.
     *
     * @param myClassNode MyClassNode representing the class.
     * @param classString StringBuilder to append the class information.
     * @param classType The type of class (e.g., class, abstract class, interface).
     */
    private void convertInnerClassInfo(MyClassNode myClassNode, StringBuilder classString, String classType) {
        String className = myClassNode.name.substring(myClassNode.name.lastIndexOf("$") + 1);
        MyInnerClassNode innerClassNode = findInnerClassNode(myClassNode, myClassNode.name);
        if(innerClassNode != null){
            String classModifier = getAccessModifier(innerClassNode.access);
            classString.append(classModifier).append(classType).append(" ").append(className);
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
