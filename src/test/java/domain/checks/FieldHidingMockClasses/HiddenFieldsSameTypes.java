package domain.checks.FieldHidingMockClasses;

public class HiddenFieldsSameTypes {
    String similarName;
    String differentName;
    String slightlySimilar;
    String anotherSimilar;
    public void doNothing(String similarName, String sligtlySimilar){
    }


    public void doMoreNothing(String anotherSimilar, String similarName, String slightlySimilar){
    }
}
