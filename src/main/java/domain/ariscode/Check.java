package domain.ariscode;

import java.util.List;

public interface Check {

    public List<Message> run(MyClassNode classNode);
}
