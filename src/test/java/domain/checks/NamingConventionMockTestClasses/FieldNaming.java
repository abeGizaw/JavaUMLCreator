package domain.checks.NamingConventionMockTestClasses;

public class FieldNaming {

    private String name;

    public int numberValid;

    protected String INVALID_FIELD;
    public int AlsoInvalid;


    FieldNaming(String n, int num){
        name = n;
        numberValid = num;
    }

    public String toString(){
        return String.format("Name: %s   Number: %d\n", name, numberValid);
    }
}
