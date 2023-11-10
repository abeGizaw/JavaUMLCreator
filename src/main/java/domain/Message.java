package domain;
public class Message {
    private final LintType lintType;
    private final String message;
    private final String classOfInterest;

    public Message(LintType cType, String mess, String classNameOfInterest){
            this.lintType = cType;
            this.message = mess;
            this.classOfInterest = classNameOfInterest;
        }
        public LintType getCheckType(){
            return lintType;
        }
        public String getMessage(){
            return  message;
        }
        public String getClassesOfInterest(){
            return classOfInterest;
        }
        public String toString(){
            return "Class: " + classOfInterest + "\n  Check Type: " + lintType + "\n  Message: " + message;
        }
    }

