package domain.checks.FieldHidingMockClasses;

import java.util.Deque;
import java.util.List;

public class HiddenFieldsVaryTypes {
    String similarName;
    int differentName;
    List<String> slightlySimilar;
    Deque<Integer> anotherSimilar;
    public void doNothing(String similarName, String sligtlySimilar){
    }


    public void doMoreNothing(Deque<Integer> anotherSimilar, List<String> similarName, int slightlySimilar){
    }
}
