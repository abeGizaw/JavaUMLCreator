package domain.constants;

import presentation.ANSIColors;

import java.io.File;

public class Constants {
    private static final String MULTIPLE_DIR_WARNING = "The name %s will create multiple folders due to the " + File.separatorChar;
    public static final String INVALID_PUML_PATH = "The path cannot contain a file seperator. Please try again";
    public static final String PUML_TYPE = ".puml";
    public static final String OUTPUT_DIRECTORY_FOR_CHECKS = "Please enter an output NAME for the DIRECTORY you want to put the diagrams in (not the path):";
    public static final String OUTPUT_FOR_PUML_CLASSDIAGRAM = "Please enter a name for the PUML class diagram file:";
    public static final String CHOICE_FOR_JSON = "Do you want to include Json Files? (y/N)";
    public static final String JSON_PACKAGE = "Enter package where json files are (Absolute Path)";
    public static final String ABBREVIATION_ERROR = "Invalid Input. Please Enter Abbreviations. ";
    public static final String INVALID_PACKAGE = "Invalid package. Path could not be found";
    public static final String INVALID_ARRAY_ENTRY = ">;";
    public static final String END_JSON_CHAR = "]";
    public static final String END_JSON_DIAGRAM = "@endjson";
    public static final String START_JSON_DIAGRAM = "@startjson";
    public static final String ASK_FOR_JSON_PATH = "Enter Absolute path of the jar to Directory/Package: ";
    public static final String ASK_FOR_DEFAULT = "Do you want default naming for conversion?(y/N)";

    public static String getWarningMessage(String outputPath){
        return String.format(MULTIPLE_DIR_WARNING, outputPath);
    }

}
