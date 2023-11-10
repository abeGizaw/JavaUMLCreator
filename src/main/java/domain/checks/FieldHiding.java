package domain.checks;

import domain.*;

import java.util.*;

import static presentation.ANSIColors.*;

public class FieldHiding implements Check{

    public List<Message> run(MyClassNode myClassNode) {
        return checkFieldHiding(myClassNode);
    }

    private List<Message> checkFieldHiding(MyClassNode classNode) {
        List<Message> hiddenFields = new ArrayList<>();
        Set<String> allFields = new HashSet<>();

        for (MyFieldNode field : classNode.fields) {
            allFields.add(field.name);
        }

        for (MyMethodNode method : classNode.methods) {
            if (method.localVariables != null) {
                for (MyLocalVariableNode variable : method.localVariables) {
                    if (allFields.contains(variable.name)) {
                        String message = "Field " + variable.name + " is hidden by method " + method.name;
                        hiddenFields.add(new Message(LintType.HIDDEN_FIELDS, message, classNode.name));
                    }
                }
            }
        }

        return hiddenFields;
    }
}
