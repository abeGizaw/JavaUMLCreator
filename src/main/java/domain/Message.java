package domain;

import java.util.Objects;

public class Message {
    private final LintType lintType;
    private final String message;
    private final String classOfInterest;

    public Message(LintType cType, String mess, String classNameOfInterest) {
        this.lintType = cType;
        this.message = mess;
        this.classOfInterest = classNameOfInterest;
    }

    public LintType getCheckType() {
        return lintType;
    }

    public String getMessage() {
        return message;
    }

    public String getClassesOfInterest() {
        return classOfInterest;
    }

    public String toString() {
        return "Class: " + classOfInterest + "\n  Check Type: " + lintType + "\n  Message: " + message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Message message = (Message) obj;
        return Objects.equals(this.getCheckType(), message.getCheckType()) &&
                Objects.equals(this.message, message.message) &&
                Objects.equals(this.classOfInterest, message.getClassesOfInterest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(lintType, message, classOfInterest);
    }
}

