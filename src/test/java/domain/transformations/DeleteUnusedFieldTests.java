package domain.transformations;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeleteUnusedFieldTests {

    @Test
    public void runDetect() throws IOException {
        List<String> classNames = new ArrayList<>();
        classNames.add("domain/transformations/UnusedFieldMockTestClasses/UnusedField");
        classNames.add("domain/transformations/UnusedFieldMockTestClasses/UnusedFieldSupport");
        String outputPath = "src/test/java/domain/transformations/UnusedFieldMockTestClasses";
        List<ClassNode> nodes = new ArrayList<>();

        for (String className : classNames) {
            ClassNode classNode = new ClassNode();
            try {
                ClassReader reader = new ClassReader(className);
                reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            } catch (IOException e) {
                throw new RuntimeException("This is not a valid class name:" + e.toString());
            }
            nodes.add(classNode);
        }

        List<String> fieldsToRemove = new ArrayList<>();
        fieldsToRemove.add("unusedAndNeverSet");
        fieldsToRemove.add("unusedFieldSetInCons");
        fieldsToRemove.add("unusedSupportLocal");
        Transformation removeUnusedField = new DeleteUnusedFields(outputPath, fieldsToRemove);
        removeUnusedField.run(nodes);

        List<String> paths= new ArrayList<>();
        paths.add("src/test/java/domain/transformations/UnusedFieldMockTestClasses/UnusedFieldSupport.class");
        paths.add("src/test/java/domain/transformations/UnusedFieldMockTestClasses/UnusedField.class");
        validateOutputFileHasNoUnusedFields(paths, fieldsToRemove);

    }

    private void validateOutputFileHasNoUnusedFields(List<String> paths, List<String> unusedToRemove) {
        for(String path: paths){
            ClassNode outputNode = new ClassNode();
            try (FileInputStream fileInputStream = new FileInputStream(path)) {
                ClassReader reader = new ClassReader(fileInputStream);
                reader.accept(outputNode, ClassReader.EXPAND_FRAMES);
            } catch (IOException e) {
                throw new RuntimeException("This is not a valid file path " + e.toString());
            }

            for (FieldNode fieldNode : outputNode.fields) {
                assertFalse(unusedToRemove.contains(fieldNode.name));
            }
            for (FieldNode fieldNode : outputNode.fields) {
                assertFalse(unusedToRemove.contains(fieldNode.name));
            }
        }
    }

}
