package domain.checks.StrategyPatternMockTestClasses;

public class InvalidStrategyPatternWSetter {
    private String name;
    public int val1;
    private ConcreteStrategy strategyField;

    InvalidStrategyPatternWSetter(String n, int value) {
        name = n;
        val1 = value;
    }

    public void setStrategyField(ConcreteStrategy s) {
        strategyField = s;
    }

    public void performStrategyMethod() {
        strategyField.strategyMethod();
    }

    public String toString(){
        return String.format("Name: %s,  Val1: %d", name, val1);
    }


}
