# Project: Linter

## Dependencies
Specified in pom.xml

## Contributors
Abe Gizaw, Ari Duvall, Kiana Fan

## Team Member's Engineering Notebooks (one per person)
- Abe Gizaw: https://docs.google.com/document/d/1QntoazmMmmHKF5ooFJrvxNsr07hzkH83dWjOtmInolo/edit?usp=sharing
- Ari Duvall: https://docs.google.com/document/d/1h6pvMb-gH99w1Slisip0Jb40IVVEtMKgQjy8QonVOeM/edit?usp=sharing
- Kiana Fan: https://docs.google.com/document/d/1X2C_6SXm9CsafaTf-roLYZjLglgAG-YzNeLyp9Adk88/edit?usp=sharing

## Features


| Developer  | Style Check                                              | Principle Check                          | Pattern Check    | A Feature (optional)            |
|:-----------|:---------------------------------------------------------|:-----------------------------------------|:-----------------|:--------------------------------|
| Abe Gizaw  | Hidden Fields                                            | Program to Interface over Implementation | Template Method  | ASM to PlantUML class Diagram   |
| Ari Duvall | Naming Convention (class, field, method, final variable) | Favor Composition over Inheritance       | Strategy         | Detect and Remove unused fields |
| Kiana Fan  | Final Local Variables                                    | Principle of Least Knowledge             | Adapter          | Principle of Least Knowledge    |


## How to Run the Linter
### Input 
When running the Linter the user will be prompted through the command line for the following information:
- File Location Information
  - Absolute path for the directory containing the .class files to process
  - Absolute path for .class files to be processed.
  - To process more than one of the options above, sepearte each path with a comma. 
- Desired Style Checks to run [Naming Convention, Final Local Variables, Hidden Fields]
- Desired Principle Checks to run [Favor Composition over Inheritance, PLK, Program to Interface not Implementation]
- Desired Pattern Checks to run [Strategy Pattern, Adapter Pattern, Template Method Pattern]

After the user enters a response, the response will be process to ensure it is a valid input. If the input is invalid the user will be prompted again. 
  

### Output 
Once the Linter completes the user selected checks, the resulting output will be displayed to both the console and a log file. 
The log file, log.txt,  can be found in the directory provided. 

All output will be formatted as follows:      

  Check Type: EXAMPLE_CHECK_TYPE     
    &nbsp; &nbsp; Class Names: ClassA       
    &nbsp; &nbsp; Message: "Check specific Message Here"        


