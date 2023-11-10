package domain;
public class Message {
    private final CheckType checkType;
    private final String message;
    private final String classOfInterest;

    public Message(CheckType cType, String mess, String classNameOfInterest) {
        this.checkType = cType;
        this.message = mess;
        this.classOfInterest = classNameOfInterest;
    }
    
    public CheckType getCheckType(){
        return checkType;
    }

    public String getMessage(){
        return  message;
    }

    public String getClassesOfInterest(){
        return classOfInterest;
    }

    public String toString(){
        return "Classes: " + classOfInterest + "\n  Check Type: " + checkType + "\n  Message: " + message;
    }

}