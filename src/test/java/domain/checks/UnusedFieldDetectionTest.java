package domain.checks;

import domain.Message;
import domain.MyClassNodeCreator;
import domain.arisCode.QD_UnusedFields;
import domain.myasm.MyASMClassNodeCreator;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnusedFieldDetectionTest {
    private final MyClassNodeCreator creator = new MyASMClassNodeCreator();

    @Test
    public void runDetect() throws IOException {
        String className1 = "domain/checks/UnusedFieldMockTestClasses/UnusedField";
        String className2 = "domain/checks/UnusedFieldMockTestClasses/UnusedFieldSupport";
//        MyClassNode classNode1 = creator.createMyClassNodeFromName(className1);
//        MyClassNode classNode2 = creator.createMyClassNodeFromName(className2);

        ClassNode classNode1;
        ClassNode classNode2;
        try {
            ClassReader reader1 = new ClassReader(className1);
            ClassReader reader2 = new ClassReader(className2);
            classNode1 = new ClassNode();
            classNode2 = new ClassNode();
            reader1.accept(classNode1, ClassReader.EXPAND_FRAMES);
            reader2.accept(classNode2, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            throw new RuntimeException("This is not a valid class name:" + e.toString());
        }

        List<ClassNode> classNodes = new ArrayList<>();
        classNodes.add(classNode1);
        classNodes.add(classNode2);
        String outputPath = "src/main/java/domain/arisCode";
        QD_UnusedFields qd_unusedFields = new QD_UnusedFields(classNodes, outputPath);
        List<Message> messageList = qd_unusedFields.run();

        System.out.println();
        System.out.println("MESSAGES ");
        printMessages(messageList);
    }


    private static void printMessages(List<Message> messageList) {
        for (Message message : messageList) {
            System.out.println(message.toString());
        }
        System.out.println("Total Messages: " + messageList.size());
    }


    private void printAllFields(ClassNode classNode) {
        System.out.println("Class: " + classNode.name);
        for (FieldNode fieldNode : classNode.fields) {
            System.out.println("    " + fieldNode.name);
        }

        for (MethodNode method : classNode.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction.getOpcode() == Opcodes.PUTFIELD || instruction.getOpcode() == Opcodes.PUTSTATIC) {
                    FieldInsnNode node = (FieldInsnNode) instruction;
                    System.out.println("    Assigning: " + node.name);
                }
            }
        }
    }
}


