package domain;

import org.objectweb.asm.Opcodes;

public interface MyOpcodes {
    int ACC_FINAL = 16;
    int INVOKEVIRTUAL = 182;
    int INVOKESPECIAL = 183;
    int INVOKEINTERFACE = 185;
    int INVOKEDYNAMIC = 186;
    int ISTORE = 54;
    int LSTORE = 55;
    int FSTORE = 56;
    int DSTORE = 57;
    int ASTORE = 58;
    int ACC_ABSTRACT = 1024;
    int ACC_INTERFACE = 512;
}
