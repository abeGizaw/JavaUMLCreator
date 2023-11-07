package domain.checks;

import domain.CheckType;
import domain.Message;
import domain.MyClassNode;

import java.util.ArrayList;
import java.util.List;

public class FavorCompOverInheritance implements Check {
    public List<Message> run(MyClassNode classNode) {
        return checkForInheritance(classNode);
    }

    private List<Message> checkForInheritance(MyClassNode classNode) {
        List<Message> messages = new ArrayList<>();
        String message = "";

        String superName = classNode.superName;
        String[] parts = classNode.superName.split("/");
        if (!parts[0].isEmpty() && !parts[0].equals("java")) {
            message = "Consider composition instead of inheritance. " + classNode.name + " EXTENDS " + superName;
            messages.add(new Message(CheckType.COMPOSITION_OVER_INHERITANCE, message, classNode.name));
        }
        return messages;
    }
}
