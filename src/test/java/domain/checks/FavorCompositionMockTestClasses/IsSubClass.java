package domain.checks.FavorCompositionMockTestClasses;

public class IsSubClass extends SuperClass{

    private String name = "";
    private int val;

    IsSubClass(String n, int v){
        name = n;
        val  = v;
    }

    @Override
    public void inheritMethod() {

    }

    public String myMethod(){
        return name;
    }
}
