package domain.checks.StrategyPatternMockTestClasses;

public class ValidStrategyPatternInterfaceWSetter {
    private String name;
    public int val1;
    private StrategyInterface strategyField;

    ValidStrategyPatternInterfaceWSetter(String n, int value) {
        name = n;
        val1 = value;
    }

    public void setStrategyField(StrategyInterface s) {
        strategyField = s;
    }

    public void performStrategyMethod() {
        strategyField.performStrategyMethod();
    }

    public String toString(){
        return String.format("Name: %s,  Val1: %d", name, val1);
    }


}
