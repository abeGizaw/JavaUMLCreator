package domain.checks.ProgramToInterfaceNotImplementationTestClasses;

import domain.checks.ProgramToInterfaceNotImplementationTestClasses.abstractmock.Dog;
import domain.checks.ProgramToInterfaceNotImplementationTestClasses.abstractmock.Pet;
import java.util.*;

public class GoodInterfaceUse {
    Set<String> goodInterfaceUse = new HashSet<>();
    Pet goodAbstractUse = new Dog();
    AbstractList<String> followsPattern = new ArrayList<>();
    Map<String, String> great = new HashMap<>();
}
