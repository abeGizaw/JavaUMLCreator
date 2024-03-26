package domain.diagramconverter;

import domain.MyClassNode;
import domain.MyFieldNode;
import domain.MyOpcodes;

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

    private void appendFieldInfo(StringBuilder fieldString, MyFieldNode field, String className, RelationsManager relationsManager) {
        if (isSynthetic(field.access)) {
            return;
        }

        if ((field.access & MyOpcodes.ACC_ENUM) != 0 && (field.access & MyOpcodes.ACC_STATIC) != 0) {
            fieldString.append(field.name).append("\n\t");
            return;
        }

        String accessModifier = getAccessModifier(field.access);
        String nonAccessModifier = getNonAccessModifiers(field.access);
        fieldString.append(accessModifier).append(nonAccessModifier);

        String fullDesc = (field.signature != null) ? field.signature : field.desc;
        String descName = getFieldType(fullDesc);



        if(!isJavaAPIClass(fullDesc, className)){
            String cleanedDescName = removeBracketsFromDesc(descName);
            relationsManager.addAHasARelationship(cleanedDescName, className, isCollectionType(descName));
        }
        fieldString.append(" ").append(field.name).append(": ").append(descName).append("\n\t");
    }


    /**
     *
     * @param descName exmaple: Set<MockAbstract>
     *                          HashMap<MockEnum,MockInterface>
     *                          AccessModifierVariety[]
     * @return example: MockAbstract
     *                  MockEnum,MockInterface
     *                  AccessModifierVariety
     */
    private String removeBracketsFromDesc(String descName) {
        if(descName.endsWith("[]")){
            return removeBracketsFromDesc(descName.substring(0, descName.length() - 2));
        } else if(descName.contains("<")){
            while(descName.contains("<")){
                descName = descName.substring(descName.indexOf("<") + 1);
            }
            descName = descName.substring(0, descName.indexOf(">"));
            descName = descName.replace("[]", "");
        }

        return descName;
    }


    private boolean isSynthetic(int access) {
        return (access & MyOpcodes.ACC_SYNTHETIC) != 0;
    }
}
