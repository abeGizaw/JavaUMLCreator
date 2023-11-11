package domain.umlconverter;

public class ImplementingClass implements MockInterface {

    @Override
    public double calculate(double input) {
        return input * 2;
    }

    @Override
    public String transform(String input) {
        return input.toUpperCase();
    }
}
