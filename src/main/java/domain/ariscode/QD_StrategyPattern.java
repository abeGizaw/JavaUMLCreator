package domain.ariscode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.List;


public class QD_StrategyPattern {
    private final ClassNode classNode;

    QD_StrategyPattern(ClassNode c) {
        classNode = c;
    }

    public void run() {
        checkForStrategyPattern();
    }

    private void checkForStrategyPattern() {
        List<FieldNode> fields = (List<FieldNode>) classNode.fields;
        for (FieldNode field : fields) {
            String[] parts = field.desc.substring(1).split("/");
            if (!parts[0].equals("java") && !parts[0].isEmpty()) {
                checkFieldForStrategyPattern(field);
            }
        }

    }

    private void checkFieldForStrategyPattern(FieldNode field) {
        if (fieldIsAbstractAndValidClass(field.desc.substring(1, (field.desc.length() - 1)), classNode.name, field.name)) {
            String setterName = findSetter(classNode, field.name, field.desc);
            if (!setterName.isEmpty()) {
                System.out.printf("STRATEGY PATTERN: %s stores an instance of  %s in the field %s. The setter is %s. \n",
                        classNode.name, field.desc, field.name, setterName);
            }
        }
    }

    private boolean fieldIsAbstractAndValidClass(String name, String originalClassName, String fieldName) {
        ClassReader reader = null;
        try {
            reader = new ClassReader(name);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            return fieldIsAbstractType(classNode, originalClassName, fieldName);

        } catch (IOException e) {
            System.out.println("Could not Find: " + name);
        }
        return false;
    }

    private boolean fieldIsAbstractType(ClassNode classNode, String originalClassName, String fieldName) {
        return (((classNode.access & Opcodes.ACC_ABSTRACT) != 0) || ((classNode.access & Opcodes.ACC_INTERFACE) != 0));
    }

    private String findSetter(ClassNode classNode, String fieldName, String fieldType) {
        for (MethodNode methodNode : classNode.methods) {
            for (AbstractInsnNode instruction : methodNode.instructions) {
                if (instruction.getOpcode() == Opcodes.PUTFIELD || instruction.getOpcode() == Opcodes.PUTSTATIC) {
                    FieldInsnNode fieldInsn = (FieldInsnNode) instruction;
                    if (fieldInsn.name.equals(fieldName) && fieldInsn.desc.equals(fieldType)) {
                        return methodNode.name;
                    }
                }
            }
        }
        return "";
    }
}