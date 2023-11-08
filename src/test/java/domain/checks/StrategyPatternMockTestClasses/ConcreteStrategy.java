package domain.checks.StrategyPatternMockTestClasses;

public class ConcreteStrategy extends StrategyAbstractClass{

    public String concreteVal;

    @Override
    public void strategyMethod() {

    }

    public String getConcreteVal(){
        return concreteVal;
    }

    public void setConcreteVal(String val){
        concreteVal = val;
    }
}
