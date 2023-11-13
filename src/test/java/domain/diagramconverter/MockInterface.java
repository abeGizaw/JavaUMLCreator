package domain.diagramconverter;

public interface MockInterface {

    double calculate(double input);

    String transform(String input);
    default void show() {
        System.out.println("This is from AdvancedInterface.");
    }

    static void helper() {
        System.out.println("Helper static method from AdvancedInterface.");
    }
}
