package domain.myasm;

import domain.MyType;
import org.objectweb.asm.Type;

public class MyASMType extends MyType {
    private Type type;

    public MyASMType(Type type) {
        this.type = type;
    }

    public MyASMType() {
        this(null);
    }

    @Override
    public MyType[] getArgumentTypes() {
        if (type != null) {
            Type[] argumentTypes = type.getArgumentTypes();
            MyType[] myArgumentTypes = new MyType[argumentTypes.length];
            for (int i = 0; i < argumentTypes.length; i++) {
                myArgumentTypes[i] = new MyASMType(argumentTypes[i]);
            }
            return myArgumentTypes;
        }
        return new MyType[]{};
    }

    @Override
    public MyType getType(String typeDescriptor) {
        return new MyASMType(Type.getType(typeDescriptor));
    }
}
