package domain.checks.FavorCompositionMockTestClasses;

public class ValidComp {

    private String name = "";
    private int val;
    public SuperClass compSuper;

    ValidComp(String n, int v, SuperClass s){
        name = n;
        val  = v;
        compSuper = s;
    }

    public void runCompMethod (){
        compSuper.inheritMethod();
    }

    public String myMethod(){
        return name;
    }

}
