package domain.diagramconverter;

import java.io.FileWriter;
import java.io.IOException;

public class MakeUmlMock {
    public static void main(String[] args) {
        generatePumlForClass("ExampleClass", "AnotherClass");
    }

    public static void generatePumlForClass(String... classNames) {
        StringBuilder pumlContent = new StringBuilder();
        pumlContent.append("@startuml\n");

        pumlContent.append("class attempt {}\n");


        pumlContent.append("@enduml");

        try (FileWriter fileWriter = new FileWriter("output.puml")) {
            fileWriter.write(pumlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
