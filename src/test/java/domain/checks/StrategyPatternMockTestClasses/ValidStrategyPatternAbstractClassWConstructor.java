package domain.checks.StrategyPatternMockTestClasses;

public class ValidStrategyPatternAbstractClassWConstructor {
    private String name;
    public int val1;
    private StrategyAbstractClass strategy;

    ValidStrategyPatternAbstractClassWConstructor(String n, int value, StrategyAbstractClass s) {
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
