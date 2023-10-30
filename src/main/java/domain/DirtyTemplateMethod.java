package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DirtyTemplateMethod {
    public void run(){
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter FilePath (Design Pattern): ");
        // target/classes/domain/templatemethodmocks/CorrectTemplateMethodMock.class
        // target/classes/domain/templatemethodmocks/NoFinalTemplateMethodMock.class
        // target/classes/domain/templatemethodmocks/NoAbstractTemplateMock.class

        String filePath = keyboard.nextLine();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ClassReader myReader = new ClassReader(fileInputStream);
            ClassNode myClassNode = new ClassNode();
            myReader.accept(myClassNode, ClassReader.EXPAND_FRAMES);
            boolean usesTemplate = detectTemplateMethod(myClassNode);

            if(usesTemplate){
                System.out.println("The Class "+ myClassNode.name + " uses the templateMethod Pattern");
            } else {
                System.out.println("The Class "+ myClassNode.name + " does not use the templateMethod Pattern");
            }



        } catch (IOException e) {
            System.err.println("Error reading class file");
        }
    }

    private boolean detectTemplateMethod(ClassNode myClassNode) {
        if((myClassNode.access & Opcodes.ACC_ABSTRACT) == 0){
            return false;
        }
        for(MethodNode method: myClassNode.methods){
            if((method.access & Opcodes.ACC_FINAL) != 0 && containsAbstractMethodCall(method.instructions, myClassNode.methods)){
                return true;
            }
        }

        return false;
    }

    private boolean containsAbstractMethodCall(InsnList instructions, List<MethodNode> methods) {
        for(AbstractInsnNode node: instructions){
            if(isAMethodCall(node)){
                MethodInsnNode methodInsnNode = (MethodInsnNode) node;

                for (MethodNode methodNode : methods) {
                    if (methodNode.name.equals(methodInsnNode.name) && methodNode.desc.equals(methodInsnNode.desc)) {
                        if ((methodNode.access & Opcodes.ACC_ABSTRACT) != 0) {
                            return true;
                        }
                    }
                }

            }
        }

        return false;
    }

    private boolean isAMethodCall(AbstractInsnNode node) {
        int opcode = node.getOpcode();
        return opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKEINTERFACE ||
                opcode == Opcodes.INVOKESPECIAL || opcode == Opcodes.INVOKEDYNAMIC;
    }

}
