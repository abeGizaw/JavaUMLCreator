package datasource;

import java.io.FileWriter;
import java.io.IOException;

public class DiagramLogger implements Logger{

    private final String outputPath;

    public DiagramLogger(String outPutPath){
        this.outputPath = outPutPath;
    }
    @Override
    public void write(StringBuilder diagramInfo, String fileType) {
        try (FileWriter fileWriter = new FileWriter(outputPath + fileType)){
            fileWriter.write(diagramInfo.toString());
        } catch (IOException e) {
            System.err.println("Error writing UML to output file");
        }
    }
}
