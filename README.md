# Project: Linter

## Dependencies
Specified in pom.xml
Java: 11

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
  - To process more than one of the options above, separate each path with a comma and no spaces. 
- Desired Style Checks to run [Naming Convention, Final Local Variables, Hidden Fields]
- Desired Principle Checks to run [Favor Composition over Inheritance, PLK, Program to Interface not Implementation]
- Desired Pattern Checks to run [Strategy Pattern, Adapter Pattern, Template Method Pattern]
  - An extra prompt will appear if the Strategy Pattern or Program to Interface not Implementation is chosen due to incompatibility with Third-party libraries.
- Transformations to run [Remove Unused Fields]
- Diagrams to generate [UMLCLASS]
  - Diagrams will also prompt the user for a filename for the diagram    

After the user enters a response, the response will be processed to ensure it is a valid input. If the input is invalid the user will be prompted again. 

Example Input      
> -> Enter Directory/Package:         
     &nbsp;  &nbsp; &nbsp; C:\Users\duvallar\OneDrive\1.RoseHulman\3.Junior\Fall\CSSE374\project-202410-team02-202410\target\classes\domain\ariscode\testclasses  
> -> Please enter an output file path  
     &nbsp;  &nbsp; &nbsp; C:\Users\duvallar\OneDrive\1.RoseHulman\3.Junior\Fall\CSSE374\project-202410-team02-202410  
> -> Enter Style Checks to run separated by comma:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Naming Convention (NC), Final Local Variables (FLV), Hidden Fields (HF), Unused Fields (UF), ALL, NONE  
     &nbsp; &nbsp; &nbsp; NC,HF    
> -> Enter Pattern Checks to run separated by comma:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Strategy Pattern (SP), Adapter Pattern (AP) , Template Method Pattern (TMP), ALL, NONE  
     &nbsp; &nbsp; &nbsp; ALL   
> -> Enter Principle Checks to run separated by comma:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Favor Composition over Inheritance (FCOI) , PLK (PLK), Program to Interface not Implementation (PINI), ALL, NONE  
     &nbsp; &nbsp; &nbsp; PINI  
> -> This check (Either Strategy pattern or PINI) is not compatible with any file that references a third party class. Proceed? (y/n)  
     &nbsp; &nbsp; &nbsp; y  
> -> Enter Transformations to run:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Remove Unused Fields (RUF), NONE  
     &nbsp; &nbsp; &nbsp; NONE  
> -> Enter Diagrams to generate:  
     &nbsp;  &nbsp; &nbsp; &nbsp; UML Class Diagram (UMLCLASS), NONE  
     &nbsp; &nbsp; &nbsp; umlclass  
> -> Please enter an output file path for PUML class diagram  
     &nbsp;  &nbsp; &nbsp; &nbsp; design  

  
### Output 
Once the Linter completes the user selected checks, the resulting output will be displayed to both the console and a log file. Diagrams won't be in the console, just the log file.
The log file, `linter_log.txt`, and any diagrams generated can be found in the directory provided. 

All output will be formatted as follows:      

 > Check Type: EXAMPLE_CHECK_TYPE     
    &nbsp; &nbsp; Class Names: ClassA       
    &nbsp; &nbsp; Message: "Check specific Message Here"        

## How to Run Tests for All Checks
> -> Enter Directory/Package:    
     &nbsp;  &nbsp; &nbsp; absolutePathToTestAllChecks/testallchecks  
> -> Please enter an output file path  
     &nbsp;  &nbsp; &nbsp; absolutePathToTestAllChecks/testallchecks  
> -> Enter Style Checks to run separated by comma:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Naming Convention (NC), Final Local Variables (FLV), Hidden Fields (HF), Unused Fields (UF), ALL, NONE  
     &nbsp;  &nbsp; &nbsp; ALL  
> -> Enter Pattern Checks to run separated by comma:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Strategy Pattern (SP), Adapter Pattern (AP) , Template Method Pattern (TMP), ALL, NONE  
     &nbsp; &nbsp; &nbsp; ALL  
> -> Enter Principle Checks to run separated by comma:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Favor Composition over Inheritance (FCOI) , PLK (PLK), Program to Interface not Implementation (PINI), ALL, NONE  
     &nbsp; &nbsp; &nbsp; ALL  
> -> Enter Transformations to run:  
     &nbsp;  &nbsp; &nbsp; &nbsp; Remove Unused Fields (RUF), NONE  
     &nbsp; &nbsp; &nbsp; RUF  
> -> Enter Diagrams to generate:  
     &nbsp;  &nbsp; &nbsp; &nbsp; UML Class Diagram (UMLCLASS), NONE  
     &nbsp; &nbsp; &nbsp; umlclass  
> -> Please enter an output file path for PUML class diagram  
     &nbsp;  &nbsp; &nbsp; &nbsp; design  
