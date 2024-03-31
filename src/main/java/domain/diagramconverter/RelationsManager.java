package domain.diagramconverter;

import domain.MyClassNode;
import domain.myasm.MyASMAnnotationNode;

import java.util.*;

public class RelationsManager {
    Map<String, Integer> hasARelationShipByClass = new HashMap<>();
    Set<String> allRelationships = new HashSet<>();
    
    protected void addImplementsRelationShip(MyClassNode myClassNode, String cleanClassName){
        if(!myClassNode.interfaces.isEmpty()){
            for(String classInterface : myClassNode.interfaces){
                if(!classInterface.startsWith("java")){
                    String interfaceName = classInterface.substring(classInterface.lastIndexOf('/') + 1);
                    allRelationships.add(cleanClassName + "..|>" + interfaceName);
                }
            }
        }
    }
    protected void addDependsOnARelationShip(String className, String dependent){
        for (String field: dependent.split(",")) {
            if(!className.equals(field)){
                allRelationships.add(className + "..>" + field);
            }
        }

    }
    protected void addExtendsRelationShip(MyClassNode myClassNode, String cleanClassName){
        String abstractClass = myClassNode.superName;
        if(!abstractClass.isEmpty()){
            if(!abstractClass.startsWith("java")){
                String abstractClassName = abstractClass.substring(abstractClass.lastIndexOf('/') + 1);
                allRelationships.add(cleanClassName + "--|>" + abstractClassName);
            }
        }
    }

    private Set<String> convertHasAKeyNames(){
        Set<String> results = new HashSet<>();

        for(String relation: this.hasARelationShipByClass.keySet()){
            if(this.hasARelationShipByClass.get(relation) == 1){
                results.add(relation);
            } else {
                int count = this.hasARelationShipByClass.get(relation);
                String relWithNum = relation.substring(0, relation.indexOf('>') + 1)
                        + "\"" + count + "\"" +
                        relation.substring(relation.indexOf('>') + 1);

                results.add(relWithNum);
            }
        }
        return results;

    }

    protected void addAHasARelationship(String descName, String className, boolean collectionType) {
        for (String field: descName.split(",") ){
            if(field.contains("java")){
                continue;
            }
            String cleanClassName = className.substring(className.lastIndexOf("/") + 1);
            String baseRelationShip = cleanClassName + "-->";
            String relationship = baseRelationShip + field;
            String multipleRelationship = baseRelationShip + "\"*\"" + field;

            if (collectionType) {
                hasARelationShipByClass.remove(relationship);
                hasARelationShipByClass.putIfAbsent(multipleRelationship, 1);
            } else {
                if (!hasARelationShipByClass.containsKey(multipleRelationship)) {
                    if (hasARelationShipByClass.containsKey(relationship)) {
                        hasARelationShipByClass.put(relationship, hasARelationShipByClass.get(relationship) + 1);
                    } else {
                        hasARelationShipByClass.put(relationship, 1);
                    }

                }
            }
        }
    }

    protected String addAllRelations() {
        StringBuilder relationshipContent = new StringBuilder();
        allRelationships.addAll(convertHasAKeyNames());
        for(String relationship: allRelationships){
            relationshipContent.append(relationship).append("\n");
        }

        return relationshipContent.toString();
    }


    public void addAnnotationRelationship(List<String> annotationNames, String cleanClassName) {
        for (String annotationName:annotationNames){
            String cleanAnnName = annotationName.substring(0, annotationName.length() - 1);
            allRelationships.add(cleanClassName + "..>" + cleanAnnName + " : << uses >>");
        }
    }
}
