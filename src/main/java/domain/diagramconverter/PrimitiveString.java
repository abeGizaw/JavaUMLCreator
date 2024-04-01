package domain.diagramconverter;

import java.util.ArrayList;
import java.util.List;
// TODO CHANGE THIS TO BE AN ENUM
public interface PrimitiveString {
    String getType();
    boolean checkDesc(String desc);
}

class ByteType implements PrimitiveString {
    @Override
    public String getType() {
        return "byte";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("B");
    }

}

class CharType implements PrimitiveString {
    @Override
    public String getType() {
        return "char";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("C");
    }
}

class DoubleType implements PrimitiveString {
    @Override
    public String getType() {
        return "double";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("D");
    }
}

class FloatType implements PrimitiveString {
    @Override
    public String getType() {
        return "float";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("F");
    }
}

class IntType implements PrimitiveString {
    @Override
    public String getType() {
        return "int";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("I");
    }
}

class LongType implements PrimitiveString {
    @Override
    public String getType() {
        return "long";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("J");
    }
}

class ShortType implements PrimitiveString {
    @Override
    public String getType() {
        return "short";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("S");
    }
}

class BooleanType implements PrimitiveString {
    @Override
    public String getType() {
        return "boolean";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("Z");
    }
}

class VoidType implements PrimitiveString {
    @Override
    public String getType() {
        return "void";
    }

    @Override
    public boolean checkDesc(String desc) {
        return desc.equals("V");
    }
}
class PrimitiveStringFactory {

    private final List<PrimitiveString> primitiveTypes = new ArrayList<>();
    public PrimitiveStringFactory(){
        primitiveTypes.add(new ByteType());
        primitiveTypes.add(new CharType());
        primitiveTypes.add(new DoubleType());
        primitiveTypes.add(new FloatType());
        primitiveTypes.add(new IntType());
        primitiveTypes.add(new LongType());
        primitiveTypes.add(new ShortType());
        primitiveTypes.add(new BooleanType());
        primitiveTypes.add(new VoidType());
    }

    public String getPrimitiveFieldType(String desc) {
        for (PrimitiveString type : primitiveTypes) {
            if (type.checkDesc(desc)) {
                return type.getType();
            }
        }
        return null;
    }

}



