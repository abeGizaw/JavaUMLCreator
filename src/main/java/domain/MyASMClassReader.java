package domain;

import org.objectweb.asm.ClassReader;

import java.io.IOException;

public class MyASMClassReader implements MyClassReader{
    private ClassReader classReader;
    public MyASMClassReader(String pathName) throws IOException {
        classReader = new ClassReader(pathName);
    }

    public void accept(MyClassNode fieldClassNode, int expandFrames) {

    }
}
