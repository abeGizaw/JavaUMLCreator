package NamingConventionMockTestClasses;

public class ValidNaming {

    private String name;

    public int number;

    ValidNaming(String n, int num){
        name = n;
        number = num;
    }

    public String toString(){
        return String.format("Name: %s   Number: %d\n", name, number);
    }
}
