package domain.transformations;

import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public interface Transformation {

    public abstract void run(List<ClassNode> classNodes, List<String> fieldsToDelete);

}
