package domain.diagramconverter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelationsManager {
    Map<String, Integer> hasARelationShipByClass = new HashMap<>();
    Set<String> allRelationships = new HashSet<>();

    public RelationsManager() {

    }

    public void addHasARelationShip(){}
    public void addImplementsRelationShip(){}
    public void addDependsOnARelationShip(){}
    public void addExtendsRelationShip(){}
    public String getAllRelationships(){
        return "";
    }

    private Set<String> convertKeyNames(){
        return null;
    }

}
