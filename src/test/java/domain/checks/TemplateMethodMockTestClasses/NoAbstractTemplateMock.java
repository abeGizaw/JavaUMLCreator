package domain.checks.TemplateMethodMockTestClasses;

public abstract class NoAbstractTemplateMock {
    public final void template(){
        doConcrete();
        doAHook();
        doAnotherConcrete();
    }

    private void doAnotherConcrete() {
        System.out.println("Another concrete");
    }

    private void doAHook() {
    }

    private void doConcrete() {
        System.out.println("Concrete");
    }

}
