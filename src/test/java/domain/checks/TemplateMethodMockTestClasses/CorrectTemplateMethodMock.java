package domain.checks.TemplateMethodMockTestClasses;

public abstract class CorrectTemplateMethodMock {
    final public void doStuff(){
        doConcreteThing();
        doAbstractThing();
        doAHook();
    }
    private void doAHook() {
    }
    public abstract void doAbstractThing();

    private void doConcreteThing() {
        System.out.println("Concrete");
    }
}
