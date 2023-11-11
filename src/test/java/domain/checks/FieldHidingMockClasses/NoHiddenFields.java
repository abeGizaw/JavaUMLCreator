package domain.checks.FieldHidingMockClasses;

import java.util.Deque;
import java.util.List;

public class NoHiddenFields {
    String similarName;
    int differentName;
    List<String> slightlySimilar;
    Deque<Integer> anotherSimilar;
    public void doNothing(String unique, String different){
    }


    public void doMoreNothing(Deque<Integer> myOwnDeque, String notSimilar, int neverSeenBefore){
    }
}
