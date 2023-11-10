package domain.checks.UnusedFieldMockTestClasses;

public class UnusedField {

    private int unusedFieldSetInCons;
    private final String USED_FIELD;
    public int usedNum;
    protected boolean unusedAndNeverSet;
    public int usedInOtherClass;

    UnusedField(int a, String b) {
        this.unusedFieldSetInCons = a;
        this.USED_FIELD = b;
    }


    public void printMe(String message) {
        System.out.println(message);

    }

    public String printUsed() {
        System.out.println(USED_FIELD);
        return USED_FIELD;
    }

    public int setUsedNum(int a) {
        usedNum = a;
        a += 4;
        return usedNum;
    }

}
