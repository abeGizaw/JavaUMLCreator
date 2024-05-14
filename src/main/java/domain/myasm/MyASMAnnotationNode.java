package domain.myasm;

import domain.MyAnnotationNode;
import org.objectweb.asm.tree.AnnotationNode;

public class MyASMAnnotationNode extends MyAnnotationNode {
    private AnnotationNode annotationNode;
    public MyASMAnnotationNode(AnnotationNode annotationNode){
        this.annotationNode = annotationNode;
        super.desc = annotationNode.desc;
        super.values = annotationNode.values;
    }
}
