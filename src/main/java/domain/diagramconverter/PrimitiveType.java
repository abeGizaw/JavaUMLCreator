package domain.diagramconverter;

public enum PrimitiveType {
    BYTE("byte", "B"),
    CHAR("char", "C"),
    DOUBLE("double", "D"),
    FLOAT("float", "F"),
    INT("int", "I"),
    LONG("long", "J"),
    SHORT("short", "S"),
    BOOLEAN("boolean", "Z"),
    VOID("void", "V");

    private final String type;
    private final String descriptor;

    PrimitiveType(String type, String descriptor) {
        this.type = type;
        this.descriptor = descriptor;
    }

    public String getType() {
        return type;
    }

    public static PrimitiveType fromDescriptor(String descriptor) {
        for (PrimitiveType primitive : PrimitiveType.values()) {
            if (primitive.descriptor.equals(descriptor)) {
                return primitive;
            }
        }
        throw new IllegalArgumentException("Unknown descriptor: " + descriptor);
    }
}
