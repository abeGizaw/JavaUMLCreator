package domain.checks;

import domain.*;

import java.util.ArrayList;
import java.util.List;

public class ProgramInterfaceNotImplementation implements Check {
    private final MyClassNodeCreator classNodeCreator;

    public ProgramInterfaceNotImplementation(MyClassNodeCreator nodeCreator) {
        this.classNodeCreator = nodeCreator;
    }

    public List<Message> run(MyClassNode myClassNode) {
        return checkImplementInterface(myClassNode);
    }

    private List<Message> checkImplementInterface(MyClassNode classNode) {
        List<Message> invalidUses = new ArrayList<>();

        for (MyFieldNode field : classNode.fields) {
            if (isPrimitive(field.desc)) continue;

            String className = getClassName(field.desc);

            if (isJavaAPIClass(className)) {
                readJavaDefinedClass(classNode, className, field, invalidUses);
            } else {
                readUserDefinedClass(classNode, className, field, invalidUses);
            }

        }
        return invalidUses;
    }

    private String getClassName(String desc) {
        if (desc.startsWith("[")) {
            return getClassName(desc.substring(1));
        }
        return desc.substring(1, desc.length() - 1);
    }

    boolean isPrimitive(String desc) {
        if (desc.startsWith("[")) {
            return isPrimitive(desc.substring(1));
        }
        return !desc.startsWith("L");
    }

    private boolean isJavaAPIClass(String className) {
        return className.startsWith("java/");
    }


    private void readJavaDefinedClass(MyClassNode classNode, String classNamePath, MyFieldNode field, List<Message> invalidUses) {
        MyClassNode fieldClassNode = classNodeCreator.createMyClassNodeFromName(classNamePath);
        if (implementsInterfaceOrExtendsAbstractClass(fieldClassNode)) {
            String message = "Where you need to Programming to interface instead of implementation: " + field.name;
            invalidUses.add(new Message(LintType.INTERFACE_OVER_IMPLEMENTATION, message, classNode.name));
        }
    }

    private void readUserDefinedClass(MyClassNode classNode, String className, MyFieldNode field, List<Message> invalidUses) {
        MyClassNode fieldClassNode = classNodeCreator.createUniqueMyClassNodeFromName(className);
        if (implementsInterfaceOrExtendsAbstractClass(fieldClassNode)) {
            String message = "Where you need to Programming to interface instead of implementation: " + field.name;
            invalidUses.add(new Message(LintType.INTERFACE_OVER_IMPLEMENTATION, message, classNode.name));
        }

    }

    private boolean implementsInterfaceOrExtendsAbstractClass(MyClassNode fieldClassNode) {
        if ((fieldClassNode.access & MyOpcodes.ACC_FINAL) != 0) {
            return false;
        }

        if ((fieldClassNode.access & MyOpcodes.ACC_INTERFACE) == 0 && (fieldClassNode.access & MyOpcodes.ACC_ABSTRACT) == 0) {
            return !fieldClassNode.interfaces.isEmpty() || (fieldClassNode.superName != null && checkIfAbstract(fieldClassNode.superName));
        }
        return false;
    }

    private boolean checkIfAbstract(String superName) {
        if (isJavaAPIClass(superName)) {
            MyClassNode myClassNode = classNodeCreator.createMyClassNodeFromName(superName);
            return (myClassNode.access & MyOpcodes.ACC_ABSTRACT) != 0;
        } else {
            MyClassNode myClassNode = classNodeCreator.createUniqueMyClassNodeFromName(superName);
            return (myClassNode.access & MyOpcodes.ACC_ABSTRACT) != 0;
        }
    }
}
