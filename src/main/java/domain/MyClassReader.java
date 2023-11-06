package domain;

import java.io.IOException;

public abstract class MyClassReader {
    public abstract MyClassNode generateMyClassNode(String className) throws IOException;
}
