package domain.ariscode;

import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;

public class QD_FavorCompOverInheritance implements Check {
    QD_FavorCompOverInheritance() {}

    public List<Message> run(MyClassNode classNode) {
       return checkForInheritance(classNode);
    }

    private List<Message> checkForInheritance(MyClassNode classNode) {
        List<Message> messages = new ArrayList<>();
        String message = "";

        String superName = classNode.superName;
        String[] parts = classNode.superName.split("/");
        if (!parts[0].isEmpty() && !parts[0].equals("java")) {
            message =  "Consider composition instead of inheritance. This extends " + superName;
        }
        Message result = new Message(CheckType.COMPOSITION_OVER_INHERITANCE, message, classNode.name);
        messages.add(result);
        return messages;
    }
}
