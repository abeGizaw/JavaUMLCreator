package domain.transformations;

import domain.Message;
import domain.MyClassNode;

import java.util.List;

public interface Transformation {
    /**
     * Note: Transformations take in a list of MyClassNodes so that the rest of the system can operate
     * as normal and does not need to have any knowledge of ASM. Transformations do rely on ASM and
     * these dependencies are encapsulated to the transformation package.
     *
     * @param classNodes
     * @return
     */
    public abstract List<Message> run(List<MyClassNode> classNodes);

}
