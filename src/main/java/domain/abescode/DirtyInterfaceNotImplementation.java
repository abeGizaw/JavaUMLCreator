package domain.abescode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static presentation.ANSIColors.*;

public class DirtyInterfaceNotImplementation {

    public void run(File filePath){
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ClassReader myReader = new ClassReader(fileInputStream);
            ClassNode myClassNode = new ClassNode();
            myReader.accept(myClassNode, ClassReader.EXPAND_FRAMES);

            List<String> invalidUses = checkImplementInterface(myClassNode);

            System.out.println(BLUE + "Where you are not Programming to interface, but instead implementation: " + invalidUses + RESET);


        } catch (IOException e) {
            System.err.println("Error reading class file");
        }
    }

    private List<String> checkImplementInterface(ClassNode classNode){
        List<String> invalidUses = new ArrayList<>();
        for (FieldNode field : classNode.fields) {
            String className = field.desc.substring(1, field.desc.length() - 1);

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


        return invalidUses;
    }

    private boolean implementsOrExtendsClass(ClassNode fieldClassNode) {
        if((fieldClassNode.access & Opcodes.ACC_INTERFACE) == 0 && (fieldClassNode.access & Opcodes.ACC_ABSTRACT) == 0){
            return !fieldClassNode.interfaces.isEmpty() || (fieldClassNode.superName != null && checkIfAbstract(fieldClassNode.superName));

        }
        return false;
    }

    private boolean checkIfAbstract(String superName) {
        try {
            ClassReader myReader = new ClassReader(superName);
            ClassNode myClassNode = new ClassNode();
            myReader.accept(myClassNode, ClassReader.EXPAND_FRAMES);

            if((myClassNode.access & Opcodes.ACC_ABSTRACT) != 0){
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

}
