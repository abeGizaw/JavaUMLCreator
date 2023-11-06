package domain;

import org.objectweb.asm.ClassReader;

import java.io.FileInputStream;
import java.io.IOException;

public class MyASMClassReader implements MyClassReader{
    private ClassReader classReader;
    public MyASMClassReader(String pathName) throws IOException {
        classReader = new ClassReader(pathName);
    }

    public MyASMClassReader(FileInputStream file) throws IOException {
        classReader = new ClassReader(file);
    }

    public void accept(MyClassNode fieldClassNode) {

    }
}
