package domain.checks.UnusedFieldMockTestClasses;

public class UnusedFieldSupport {
    boolean unusedSupportLocal;
    private final UnusedField u = new UnusedField(4 , "test");


    public void setUseField(){
        u.usedInOtherClass = 6;
    }

    public int getUsedField(){
        return u.usedInOtherClass;
    }
}
