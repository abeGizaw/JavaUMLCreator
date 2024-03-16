package domain.diagramconverter.ClassUmlMockTestClasses.ExtendsAndImplements;

import java.io.IOException;

public abstract class Person implements Sound, Appendable{
    @Override
    public double calculate(double input) {
        return 0;
    }

    @Override
    public String transform(String input) {
        return null;
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        return null;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return null;
    }

    @Override
    public Appendable append(char c) throws IOException {
        return null;
    }
}
