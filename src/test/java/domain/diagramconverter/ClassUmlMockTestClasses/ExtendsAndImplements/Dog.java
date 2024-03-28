package domain.diagramconverter.ClassUmlMockTestClasses.ExtendsAndImplements;

import datasource.Saver;

public class Dog extends Pet implements Sound, Saver {
    @Override
    public double calculate(double input) {
        return 0;
    }

    @Override
    public String transform(String input) {
        return null;
    }

    @Override
    public void saveMessage(String message) {

    }

    @Override
    public void writeToFile(String info, String fileType, String outputPath) {

    }
}
