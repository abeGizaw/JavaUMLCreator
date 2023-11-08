package domain.checks;

import domain.*;
import java.util.ArrayList;
import java.util.List;
import static presentation.ANSIColors.*;

public class ProgramInterfaceNotImplementation implements Check{
    private final MyClassNodeCreator classNodeCreator;
    public ProgramInterfaceNotImplementation(MyClassNodeCreator nodeCreator){
        classNodeCreator = nodeCreator;
    }

    public List<Message> run(MyClassNode myClassNode){
        return checkImplementInterface(myClassNode);
//        System.out.println(BLUE + "Where you are not Programming to interface, but instead implementation: " + invalidUses + RESET);
//        return null;
    }

    private List<Message> checkImplementInterface(MyClassNode classNode){
        List<Message> invalidUses2 = new ArrayList<>();

        for (MyFieldNode field : classNode.fields) {
            String className = field.desc.substring(1, field.desc.length() - 1);
            MyClassNode fieldClassNode = classNodeCreator.createMyClassNodeFromName(className);
            if(implementsInterfaceOrExtendsAbstractClass(fieldClassNode)) {
                String message = "Where you need to Programming to interface instead of implementation: " + field.name;
                invalidUses2.add(new Message(CheckType.INTERFACE_OVER_IMPLEMENTATION, message, classNode.name));
            }
        }
        return invalidUses2;
    }

    private boolean implementsInterfaceOrExtendsAbstractClass(MyClassNode fieldClassNode) {
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
