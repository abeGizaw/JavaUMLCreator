package domain;


import domain.abstractMock.Dog;
import domain.abstractMock.Pet;

import java.io.FileInputStream;
import java.util.*;

public class InterfaceMock {

    ArrayList<String> violatesPattern = new ArrayList<>();
    AbstractList<String> followsPattern = new ArrayList<>();
    HashMap<String, String> violator = new HashMap<>();
    Map<String, String> great = new HashMap<>();

    HashSet<String> badInterfaceUse = new HashSet<>();

    Set<String> goodInterfaceUse = new HashSet<>();

    Dog badAbstractUse = new Dog();

    Pet goodAbstractUse = new Dog();

}

