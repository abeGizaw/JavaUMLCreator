package domain;

public class AbesMain {
    public static void main(String[] args) {
        DirtyFieldHiding fieldHider = new DirtyFieldHiding();
        // target/classes/domain/FieldMock.class
        fieldHider.run();

        DirtyInterfaceNotImplementation designPrinciple = new DirtyInterfaceNotImplementation();
        // target/classes/domain/InterfaceMock.class
        designPrinciple.run();

        DirtyTemplateMethod designPattern = new DirtyTemplateMethod();
        // target/classes/domain/templatemethodmocks/CorrectTemplateMethodMock.class
        // target/classes/domain/templatemethodmocks/NoFinalTemplateMethodMock.class
        // target/classes/domain/templatemethodmocks/NoAbstractTemplateMock.class
        designPattern.run();
    }
}
