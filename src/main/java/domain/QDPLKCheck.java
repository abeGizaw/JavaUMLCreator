package domain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QDPLKCheck {
    public static void main(String[] args) throws IOException {
        List<String> classNames = new ArrayList<>();
        classNames.add("domain/QDPLKCheck");

        List<ClassNode> classNodes = new ArrayList<>();
        for (String className : classNames) {
            ClassReader reader = new ClassReader(className);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            classNodes.add(classNode);
        }

        QDPLKCheck qdPLKCheck = new QDPLKCheck();
        qdPLKCheck.checkPLK(classNodes);
    }

    private void checkPLK(List<ClassNode> classNodes) {
        for (ClassNode classNode : classNodes) {
            for (MethodNode methodNode : classNode.methods) {
                System.out.println(methodNode.name);
            }
        }
    }
}
