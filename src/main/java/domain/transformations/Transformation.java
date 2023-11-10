package domain.transformations;

import domain.Message;
import domain.MyClassNode;

import java.util.List;

public interface Transformation {

    public abstract List<Message> run(List<MyClassNode> classNodes);

}
