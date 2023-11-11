package domain.checks;
/*
  Note that this check is going to validate that proper naming convention is used for the following:
   - class names: UpperCase
   - field names: lowerCase
   - method names: lowerCase
   - final variable: all caps
 */

import domain.*;

import java.util.ArrayList;
import java.util.List;

public class NamingConventionCheck implements Check {

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
            return new Message(LintType.NAMING_CONVENTION, "Invalid Class Name: Must be in PascalCase: " + name, classNode.name);
        }
        return null;
    }


    private List<Message> checkFieldNames(MyClassNode classNode) {
        List<Message> invalidFieldMessages = new ArrayList<>();

        for (MyFieldNode field : classNode.fields) {
            if (((field.access & MyOpcodes.ACC_FINAL) != 0) && ((field.access & MyOpcodes.ACC_STATIC) != 0)) {
                if (!isAllCaps(field.name)) {
                    invalidFieldMessages.add(new Message(LintType.NAMING_CONVENTION, "Invalid Field Name: Static Final Fields must be in all caps:   " + field.name, classNode.name));
                }

            } else {
                if (invalidCamelCase(field.name)) {
                    invalidFieldMessages.add(new Message(LintType.NAMING_CONVENTION, "Invalid Field Name: Must be in camelCase:   " + field.name, classNode.name));
                }
            }
        }
        return invalidFieldMessages;
    }

    private List<Message> checkMethodName(MyClassNode classNode) {
        List<Message> invalidMethodMessages = new ArrayList<>();
        for (MyMethodNode method : classNode.methods) {
            if (invalidCamelCase(method.name) && method.name.charAt(0) != '<') {
                invalidMethodMessages.add(new Message(LintType.NAMING_CONVENTION, "Invalid method name: Must be in camelCase:  " + method.name, classNode.name));
            }
        }
        return invalidMethodMessages;
    }

    private boolean invalidCamelCase(String name) {
        return !name.matches("[a-zA-Z0-9]+(?:[a-z][a-zA-Z0-9]*|[A-Z0-9]*(?:[a-z][a-zA-Z0-9]*)?)");
    }

    private boolean invalidPascalCase(String name) {
        return !name.matches("^[A-Z][a-z]+(?:[A-Z][a-z]+)*$");
    }

    private boolean isAllCaps(String fieldName) {
        boolean isAllCaps = true;
        for (char c : fieldName.toCharArray()) {
            if (Character.isAlphabetic(c) && !Character.isUpperCase(c)) {
                isAllCaps = false;
                break;
            }
        }
        return isAllCaps;
    }

}
