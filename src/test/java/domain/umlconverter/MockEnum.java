package domain.umlconverter;

public enum MockEnum {
    RED("Red Value", 1),
    BLUE("Blue Value", 2),
    GREEN("Green Value", 3);

    private String description;
    private int code;

    MockEnum(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCode() {
        return this.code;
    }
}

