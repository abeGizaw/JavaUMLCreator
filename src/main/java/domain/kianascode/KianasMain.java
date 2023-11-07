package domain.kianascode;

import domain.MyASMClassNodeCreator;
import domain.MyClassNodeCreator;
import domain.checks.AdapterPattern;
import domain.checks.FinalLocalVariables;
import domain.checks.PrincipleOfLeastKnowledge;
import domain.MyClassNode;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class KianasMain {
    private static MyClassNodeCreator creator = new MyASMClassNodeCreator();

    public static void main(String[] args) throws IOException {
        runFinalLocalVariables();
        runAdapterPattern();
        runPLK();
    }
    
    private static void runFinalLocalVariables() throws IOException {
        String className = "domain/kianascode/FinalLocalVariablesTestClass";

        MyClassNode myClassNode = creator.createMyClassNodeFromName(className);

        FinalLocalVariables finalLocalVariablesCheck = new FinalLocalVariables();
        finalLocalVariablesCheck.run(myClassNode);
    }
    
    private static void runAdapterPattern() throws IOException {
        List<String> classNames = new ArrayList<>();
        classNames.add("domain/kianascode/ConcreteAdapter");
        classNames.add("domain/kianascode/Adaptee");
        classNames.add("domain/kianascode/Adapter");
        classNames.add("domain/kianascode/ConcreteClass1");
        classNames.add("domain/kianascode/Client");
        classNames.add("domain/kianascode/ConcreteAdapter2");
        classNames.add("domain/kianascode/Adapter2");

        List<MyClassNode> myClassNodes = new ArrayList<>();
        for (String className : classNames) {
            MyClassNode myClassNode = creator.createMyClassNodeFromName(className);
            myClassNodes.add(myClassNode);
        }

        AdapterPattern adapterPatternCheck = new AdapterPattern(myClassNodes);
        adapterPatternCheck.run(myClassNodes.get(0));
    }

    private static void runPLK() throws IOException {
        List<String> classNames = new ArrayList<>();
        classNames.add("domain/kianascode/PLKTestClass");

        PrincipleOfLeastKnowledge qdPLKCheck = new PrincipleOfLeastKnowledge();
        for (String className : classNames) {
            MyClassNode myClassNode = creator.createMyClassNodeFromName(className);

            qdPLKCheck.run(myClassNode);
        }
    }
}
