package domain;

public class AbesMain {
    public static void main(String[] args) {
        DirtyFieldHiding fieldHider = new DirtyFieldHiding();
        // target/classes/domain/FieldMock.class
        fieldHider.run();

        DirtyInterfaceNotImplementation designPattern = new DirtyInterfaceNotImplementation();
        // target/classes/domain/InterfaceMock.class
        designPattern.run();
    }
}
