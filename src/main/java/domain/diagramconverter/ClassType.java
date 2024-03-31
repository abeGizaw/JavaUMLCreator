package domain.diagramconverter;

import domain.MyOpcodes;

public enum ClassType {
    ANNOTATION(MyOpcodes.ACC_ANNOTATION, "annotation"),
    ABSTRACT_CLASS(MyOpcodes.ACC_ABSTRACT, "abstract class"),
    ENUM(MyOpcodes.ACC_ENUM, "enum"),
    INTERFACE(MyOpcodes.ACC_INTERFACE, "interface"),
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
        return CLASS; // Default return value if no other flag is set
    }

}
