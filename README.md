# JavaUMLCreator

JavaUMLCreator is a tool designed to generate UML diagrams from Java `.class` files using the ASM framework. This utility simplifies the process of visualizing the structure of Java applications and aids in the documentation and analysis of code architecture.

## Features

- **Generate UML Diagrams**: Automatically converts Java `.class` files into UML class diagrams.
- **Command-Line Interface**: Easy-to-use terminal-based interface.
- **JSON Support**: Option to include JSON files for enriched diagram generation.

## Contributors

Abe Gizaw, Ari Duvall, Kiana Fan, Ethan Huey  


## Dependencies

Specified in pom.xml  
Java: 11

## Installation

Clone the repository to your local machine:

```bash
git clone https://github.com/abeGizaw/JavaUMLCreator.git
```


## How to Run
Run the program from **src/main/java/presentation/LinterMain.java**  



#### Input
Below are the prompts you will encounter when running JavaUMLCreator, along with example inputs.


**Prompt:**
```plaintext
Enter Absolute path of the jar to Directory/Package:
-> G:/My Drive/classes/JavaProject/build/classes

Do you want default naming for conversion?(Y/N)
-> N

Please enter an output NAME for the DIRECTORY you want to put the diagrams in (not the path):
-> hola

Enter Diagrams to generate:
-> UML Class Diagram (UC), NONE

Please enter a name for the PUML class diagram file:
-> clas

Do you want to include Json Files? (Y/N)
-> Y

Enter package where json files are (Absolute Path):
-> G:/My Drive/GitHub/projects/Java/ClassUmlMockTestClasses_JSON
```

## Extra Notes  
Default naming will put your diagrams in a directory named UMLOutput  


