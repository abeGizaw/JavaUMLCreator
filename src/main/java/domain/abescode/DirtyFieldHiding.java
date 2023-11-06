package domain.abescode;

import domain.MyClassNode;
import domain.MyFieldNode;
import domain.MyLocalVariableNode;
import domain.MyMethodNode;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import java.util.*;
import static presentation.ANSIColors.*;

public class DirtyFieldHiding {

    public void run(MyClassNode myClassNode) {
        List<String> hiddenFields = checkFieldHiding(myClassNode);
        System.out.println(RED + "The Fields that are hidden: " + hiddenFields + RESET);
    }

    private List<String> checkFieldHiding(MyClassNode classNode) {
        Set<String> allFields = new HashSet<>();
        List<String> hiddenFields = new ArrayList<>();

        for (MyFieldNode field : classNode.fields) {
            allFields.add(field.name);
        }


        for (MyMethodNode method : classNode.methods) {
            if (method.localVariables != null) {
                for (MyLocalVariableNode variable : method.localVariables) {
                    if (allFields.contains(variable.name)) {
                        hiddenFields.add(variable.name);
                    }
                }
            }
        }
        return hiddenFields;
    }
}
