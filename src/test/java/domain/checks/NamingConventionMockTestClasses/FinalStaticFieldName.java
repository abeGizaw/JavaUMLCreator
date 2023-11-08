package NamingConventionMockTestClasses;

public class FinalStaticFieldName {

    private String name;

    public int number;

    public static final String invalidName = "Test";
    private static final int VALID_NUM = 4;
    protected static final boolean InvalidName2 = true;

    FinalStaticFieldName(String n, int num){
        name = n;
        number = num;
    }

    public String toString(){
        return String.format("Name: %s   Number: %d\n", name, number);
    }
}
