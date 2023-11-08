package domain.checks;
import domain.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramInterfaceNotImplementation implements Check{
    private final MyClassNodeCreator classNodeCreator;
    public ProgramInterfaceNotImplementation(MyClassNodeCreator nodeCreator){
        classNodeCreator = nodeCreator;
    }

    public List<Message> run(MyClassNode myClassNode){
        return checkImplementInterface(myClassNode);
    }

    private List<Message> checkImplementInterface(MyClassNode classNode){
        List<Message> invalidUses = new ArrayList<>();

        for (MyFieldNode field : classNode.fields) {
            String className = field.desc.substring(1, field.desc.length() - 1);
            MyClassNode fieldClassNode = classNodeCreator.createMyClassNodeFromName(className);
            if(implementsInterfaceOrExtendsAbstractClass(fieldClassNode)) {
                String message = "Where you need to Programming to interface instead of implementation: " + field.name;
                invalidUses.add(new Message(CheckType.INTERFACE_OVER_IMPLEMENTATION, message, classNode.name));
            }
        }
        return invalidUses;
    }

    private boolean implementsInterfaceOrExtendsAbstractClass(MyClassNode fieldClassNode) {
        if ((fieldClassNode.access & MyOpcodes.ACC_FINAL) != 0) {
            return false;
        }

        if((fieldClassNode.access & MyOpcodes.ACC_INTERFACE) == 0 && (fieldClassNode.access & MyOpcodes.ACC_ABSTRACT) == 0){
            return !fieldClassNode.interfaces.isEmpty() || (fieldClassNode.superName != null && checkIfAbstract(fieldClassNode.superName));
        }
        return false;
    }

    private boolean checkIfAbstract(String superName) {
        MyClassNode myClassNode = classNodeCreator.createMyClassNodeFromName(superName);
        return (myClassNode.access & MyOpcodes.ACC_ABSTRACT) != 0;
    }
}
