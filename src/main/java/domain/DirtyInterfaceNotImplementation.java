package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DirtyInterfaceNotImplementation {

    public void run(){
        Scanner keyboard = new Scanner(System.in);
        // target/classes/domain/InterfaceMock.class
        System.out.print("Enter FilePath (Design Principle): ");
        String filePath = keyboard.nextLine();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ClassReader myReader = new ClassReader(fileInputStream);
            ClassNode myClassNode = new ClassNode();
            myReader.accept(myClassNode, ClassReader.EXPAND_FRAMES);

            List<String> invalidUses = checkImplementInterface(myClassNode);

            System.out.println("Where you are not Programming to interface, but instead implementation: " + invalidUses);


        } catch (IOException e) {
            System.err.println("Error reading class file");
        }
    }

    private List<String> checkImplementInterface(ClassNode classNode){
        List<String> invalidUses = new ArrayList<>();
        if (classNode.fields != null) {
            for (FieldNode field : classNode.fields) {
//				String className = field.desc.split("/")[2];

                String className = field.desc.substring(1, field.desc.length() - 1);

//                System.out.println(className.substring(0, className.length() - 1));

                try {
                    ClassReader fieldClassReader = new ClassReader(className);
                    ClassNode fieldClassNode = new ClassNode();
                    fieldClassReader.accept(fieldClassNode, ClassReader.EXPAND_FRAMES);
                    if(implementsOrExtendsClass(fieldClassNode)){
                        invalidUses.add(field.name);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return invalidUses;
    }

    private boolean implementsOrExtendsClass(ClassNode fieldClassNode) {
        if((fieldClassNode.access & Opcodes.ACC_INTERFACE) == 0 && (fieldClassNode.access & Opcodes.ACC_ABSTRACT) == 0){
            if(!fieldClassNode.interfaces.isEmpty()){
                System.out.println("Interfaces " + fieldClassNode.name + " implements are " + fieldClassNode.interfaces);
                return true;
            }

            if(fieldClassNode.superName != null){
                System.out.println("Abstract classes " + fieldClassNode.name + " extends are " + fieldClassNode.superName);
                return true;
            }
        }
        return false;
    }

}
