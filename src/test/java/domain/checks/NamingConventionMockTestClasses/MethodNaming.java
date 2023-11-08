package domain.checks.NamingConventionMockTestClasses;

public class MethodNaming {

    private String name;

    public int number;

    MethodNaming(String n, int num){
        name = n;
        number = num;
    }

    public String toString(){
        return String.format("Name: %s   Number: %d\n", name, number);
    }

    private void INVALID_METHOD(){}

    protected void AlsoInvalid(){}

    public int validMethodUsingCamelCase(){return 4;}
}
