package domain;

import java.io.File;

public interface MyClassNodeCreator {
    MyClassNode createMyClassNodeFromName(String path);
    MyClassNode createMyClassNodeFromFile(File path);
}
