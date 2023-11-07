package domain.checks.StrategyPatternMockTestClasses;

public class ValidStrategyPatternAbstractClassWSetter {
    private String name;
    public int val1;
    private StrategyAbstractClass strategy;

    ValidStrategyPatternAbstractClassWSetter(String n, int value) {
        name = n;
        val1 = value;
    }

    public void setStrategy(StrategyAbstractClass s) {
        strategy = s;
    }

    public void performStrategyMethod() {
        strategy.strategyMethod();
    }

    public String toString(){
        return String.format("Name: %s,  Val1: %d", name, val1);
    }


}
