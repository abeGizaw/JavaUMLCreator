//package domain.abescode;
//
//import domain.*;
//import domain.myasm.MyASMClassNode;
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.Opcodes;
//import domain.MyClassNode;
//import domain.MyFieldNode;
//import domain.MyLocalVariableNode;
//import domain.MyMethodNode;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import static presentation.ANSIColors.*;
//
//public class DirtyInterfaceNotImplementation {
//
//    public void run(MyClassNode myClassNode){
//        List<String> invalidUses = checkImplementInterface(myClassNode);
//        System.out.println(BLUE + "Where you are not Programming to interface, but instead implementation: " + invalidUses + RESET);
//    }
//
//    private List<String> checkImplementInterface(MyClassNode classNode){
//        List<String> invalidUses = new ArrayList<>();
//        for (MyFieldNode field : classNode.fields) {
//            String className = field.desc.substring(1, field.desc.length() - 1);
//
//            try {
//                MyClassReader fieldClassReader = new MyASMClassReader(className);
//                MyClassNode fieldClassNode = new MyASMClassNode();
//                fieldClassReader.accept(fieldClassNode);
//                if(implementsInterfaceOrExtendsAbstractClass(fieldClassNode)){
//                    invalidUses.add(field.name);
//                }
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return invalidUses;
//    }
//
//    private boolean implementsInterfaceOrExtendsAbstractClass(MyClassNode fieldClassNode) {
//        if((fieldClassNode.access & MyOpcodes.ACC_INTERFACE) == 0 && (fieldClassNode.access & Opcodes.ACC_ABSTRACT) == 0){
//            return !fieldClassNode.interfaces.isEmpty() || (fieldClassNode.superName != null && checkIfAbstract(fieldClassNode.superName));
//
//        }
//        return false;
//    }
//
//    private boolean checkIfAbstract(String superName) {
//        try {
//            MyClassReader myReader = new MyASMClassReader(superName);
//            MyClassNode myClassNode = new MyASMClassNode();
//            myReader.accept(myClassNode);
//
//            if((myClassNode.access & MyOpcodes.ACC_ABSTRACT) != 0){
//                return true;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return false;
//    }
//}
