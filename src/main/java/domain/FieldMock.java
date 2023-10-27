package domain;

import java.util.ArrayList;
import java.util.List;

public class FieldMock {
    String similarName;
    String differentName;
    String slightlySimilar;

    String anotherSimilar;


    public void doNothing(String similarName, String sligtlySimilar, String random){
        System.out.println("Doing nothing");
    }


    public String doMoreNothing(String anotherSimilar, String similarName, String slightlySimilar){
        System.out.println("Doing more nothing");

        return "";
    }
}
