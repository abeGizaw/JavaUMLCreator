package NamingConventionMockTestClasses;

public class invalidClassName {

    private String name;

    public int number;

    invalidClassName(String n, int num){
        name = n;
        number = num;
    }

    public String toString(){
        return String.format("Name: %s   Number: %d\n", name, number);
    }
}
