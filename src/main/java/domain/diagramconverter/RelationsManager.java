package domain.diagramconverter;

import domain.MyClassNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelationsManager {
    Map<String, Integer> hasARelationShipByClass = new HashMap<>();
    Set<String> allRelationships = new HashSet<>();

    public RelationsManager() {

    }

    public void addImplementsRelationShip(MyClassNode myClassNode, String cleanClassName){
        if(!myClassNode.interfaces.isEmpty()){
            for(String classInterface : myClassNode.interfaces){
                if(!classInterface.startsWith("java")){
                    String interfaceName = classInterface.substring(classInterface.lastIndexOf('/') + 1);
                    allRelationships.add(cleanClassName + "..|>" + interfaceName);
                }
            }
        }

        // Adds the extends relations
        String abstractClass = myClassNode.superName;
        if(!abstractClass.isEmpty()){
            if(!abstractClass.startsWith("java")){
                String abstractClassName = abstractClass.substring(abstractClass.lastIndexOf('/') + 1);
                allRelationships.add(cleanClassName + "--|>" + abstractClassName);
            }
        }
    }
    public void addDependsOnARelationShip(){}
    public void addExtendsRelationShip(){}
    public String getAllRelationships(){
        return "";
    }

    private Set<String> convertKeyNames(){
        return null;
    }

    private void addAHasARelationship(String descName, String className, boolean collectionType) {
        for (String field: descName.split(",")){
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

}
