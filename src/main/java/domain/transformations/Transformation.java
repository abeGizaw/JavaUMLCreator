package domain.transformations;

import domain.Message;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public interface Transformation {

    public abstract List<Message> run(List<ClassNode> classNodes);

}
