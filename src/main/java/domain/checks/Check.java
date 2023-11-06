package domain.checks;

import domain.Message;

import java.util.List;

public interface Check {

    public List<Message> run(MyClassNode classNode);
}
