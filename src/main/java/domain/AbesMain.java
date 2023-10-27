package domain;

public class AbesMain {
    public static void main(String[] args) {
        DirtyFieldHiding fieldHider = new DirtyFieldHiding();
        fieldHider.run();

        DirtyInterfaceNotImplementation designPattern = new DirtyInterfaceNotImplementation();
        designPattern.run();
    }
}
