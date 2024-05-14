package domain.diagramconverter;

import domain.MyOpcodes;

public enum ClassType {
    ANNOTATION(MyOpcodes.ACC_ANNOTATION, "annotation"),
    INTERFACE(MyOpcodes.ACC_INTERFACE, "interface"),
    ABSTRACT_CLASS(MyOpcodes.ACC_ABSTRACT, "abstract class"),
    ENUM(MyOpcodes.ACC_ENUM, "enum"),
    RECORD(MyOpcodes.ACC_RECORD, "class"),
    EXCEPTION(0, "exception"),
    CLASS(0, "class"); // Assuming there's no specific opcode for class

    private final int opcode;
    private final String description;

    ClassType(int opcode, String desc) {
        this.opcode = opcode;
        this.description = desc;
    }

    public String getDescription(){
        return this.description;
    }

    public static ClassType getClassType(int access) {
        for (ClassType type : ClassType.values()) {
            if ((access & type.opcode) != 0) {
                return type;
            }
        }
        return CLASS;
    }
}
