package domain.checks;

import domain.Message;
import domain.MyClassNode;

import java.util.List;

public interface Check {
    public abstract List<Message> run(MyClassNode classNode);
}
