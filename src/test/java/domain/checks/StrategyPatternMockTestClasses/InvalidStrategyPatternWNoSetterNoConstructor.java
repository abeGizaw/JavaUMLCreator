package domain.checks.StrategyPatternMockTestClasses;

public class InvalidStrategyPatternWNoSetterNoConstructor {

    private String name;
    public int val1;
    private ConcreteStrategy strategyField;

    InvalidStrategyPatternWNoSetterNoConstructor(String n, int value) {
        name = n;
        val1 = value;
    }


    public void performStrategyMethod() {
        strategyField.strategyMethod();
    }

    public String toString() {
        return String.format("Name: %s,  Val1: %d", name, val1);
    }

}
