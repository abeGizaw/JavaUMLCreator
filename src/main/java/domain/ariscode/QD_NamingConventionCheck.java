package domain.ariscode;
/*
  Note that this check is going to validate that proper naming convention is used for the following:
   - class names: UpperCase
   - field names: lowerCase
   - method names: lowerCase
   - final variable: all caps
 */

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class QD_NamingConventionCheck implements Check{

    public List<Message> run(MyClassNode classNode) {
        List<Message> allMessages = new ArrayList<>();
        Message classNameMessage = checkClassName(classNode);
        if (classNameMessage != null)
            allMessages.add(classNameMessage);

        List<Message> fieldNameMessages = checkFieldNames(classNode);
        allMessages.addAll(fieldNameMessages);

        List<Message> methodNameMessages = checkMethodName(classNode);
        allMessages.addAll(methodNameMessages);
        return allMessages;
    }

    private Message checkClassName(MyClassNode classNode) {
        String[] parts = classNode.name.split("/");
        String name = parts[parts.length - 1];

        if (invalidPascalCase(name)) {
            return new Message(CheckType.NAMING_CONVENTION, "Invalid Name: Must be in PascalCase. " + name, classNode.name);
        }
        return null;
    }

//    NEED TO CONVERT OPCODES
    private  List<Message> checkFieldNames(MyClassNode classNode) {
        List<Message> invalidFieldMessages= new ArrayList<>();

        for (MyFieldNode field : classNode.fields) {
            if (((field.access & Opcodes.ACC_FINAL) != 0) && ((field.access & Opcodes.ACC_STATIC) != 0)) {
                if (!field.name.matches("[A-Z]+")) {
                    invalidFieldMessages.add(new Message(CheckType.NAMING_CONVENTION, "Invalid Field Name: Static Final Fields must be in all caps.   " + field.name, classNode.name ));
                }

            } else {
                if (invalidCamelCase(field.name)) {
                    invalidFieldMessages.add(new Message(CheckType.NAMING_CONVENTION, "Invalid Field Name: Must be in camelCase   " + field.name, classNode.name ));
                }
            }
        }
        return invalidFieldMessages;
    }

    private List<Message> checkMethodName(MyClassNode classNode) {
        List<Message> invalidMethodMessages = new ArrayList<>();
        for (MyMethodNode method : classNode.methods) {
            if (invalidCamelCase(method.name) && !method.name.equals("<init>")) {
                invalidMethodMessages.add(new Message(CheckType.NAMING_CONVENTION, "Invalid method name: Must be in camelCase.  " + method.name, classNode.name));
            }
        }
      return invalidMethodMessages;
    }

    private boolean invalidCamelCase(String name) {
        return !name.matches("^(?:[a-z]+[A-Z]?[a-zA-Z]*)+$");
    }

    private boolean invalidPascalCase(String name) {
        return !name.matches("^[A-Z][a-z]+(?:[A-Z][a-z]+)*$");
    }


}
