package domain.checks.StrategyPatternMockTestClasses;

public class InvalidStrategyPatternWConstructor {
    private String name;
    public int val1;
    private ConcreteStrategy strategy;

    InvalidStrategyPatternWConstructor(String n, int value, ConcreteStrategy s) {
        name = n;
        val1 = value;
        strategy = s;
    }

    public void performStrategyMethod() {
        strategy.strategyMethod();
    }

    public String toString(){
        return String.format("Name: %s,  Val1: %d", name, val1);
    }


}
