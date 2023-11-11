package domain.abescode.alevelfeature;

import domain.MyClassNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConvertASMToUML {
    public void run(MyClassNode classNode) {
        generateUmlDiagram(classNode);
    }

    private void generateUmlDiagram(MyClassNode myClassNode) {
        StringBuilder pumlContent = new StringBuilder();
        pumlContent.append("@startuml\n");

        
//        String[] nameProperties = myClassNode.name.split("/");
//        pumlContent.append("class ").append(nameProperties[nameProperties.length - 1]).append("{\n");
//        pumlContent.append(convertClassFields(myClassNode.fields));
//        pumlContent.append(convertClassMethods(myClassNode.methods));


        pumlContent.append("}\n");


        pumlContent.append("@enduml");

        try (FileWriter fileWriter = new FileWriter("output.puml")) {
            fileWriter.write(pumlContent.toString());
        } catch (IOException e) {
            System.err.println("Error writing UML to output file");
        }
    }

    private String convertClassName(){
        return "";
    }

    private String convertClassFields(List<FieldNode> fields) {
        return "-someValue:List<String>\n";
    }
    private String convertClassMethods(List<MethodNode> methods) {
        return "+someMethod(someParam:String, another:String):void\n";
    }

}
