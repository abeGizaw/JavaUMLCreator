package domain;

import domain.checks.*;
import domain.transformations.DeleteUnusedFields;
import domain.transformations.Transformation;

import java.nio.file.Path;
import java.util.*;

import static presentation.ANSIColors.*;

public class Linter {
    private final MyClassNodeCreator creator;
    private final List<MyClassNode> myClassNodes;
    private final Map<LintType, Check> checkTypeToCheck;
    private final Map<LintType, Transformation> transformationTypeToTransformation;

    public Linter(List<String> classPaths, MyClassNodeCreator myClassNodeCreator, String outputPath) {
        this.creator = myClassNodeCreator;
        this.myClassNodes = createClassNodes(classPaths);
        this.checkTypeToCheck = new HashMap<>();
        this.transformationTypeToTransformation = new HashMap<>();
        populateCheckMap();
        populateTransformMap(outputPath);
    }

    private void populateTransformMap(String outputPath) {
        transformationTypeToTransformation.put(LintType.UNUSED_FIELD, new DeleteUnusedFields(outputPath));
    }

    private void populateCheckMap() {
        checkTypeToCheck.put(LintType.FINAL_LOCAL_VARIABLES, new FinalLocalVariables());
        checkTypeToCheck.put(LintType.HIDDEN_FIELDS, new HiddenFields());
        checkTypeToCheck.put(LintType.NAMING_CONVENTION, new NamingConventionCheck());
        checkTypeToCheck.put(LintType.COMPOSITION_OVER_INHERITANCE, new CompositionOverInheritance());
        checkTypeToCheck.put(LintType.INTERFACE_OVER_IMPLEMENTATION, new ProgramInterfaceNotImplementation(creator));
        checkTypeToCheck.put(LintType.PLK, new PrincipleOfLeastKnowledge());
        checkTypeToCheck.put(LintType.ADAPTER_PATTERN, new AdapterPattern(myClassNodes));
        checkTypeToCheck.put(LintType.STRATEGY_PATTERN, new StrategyPattern(creator));
        checkTypeToCheck.put(LintType.TEMPLATE_METHOD_PATTERN, new TemplateMethodPattern());
        checkTypeToCheck.put(LintType.UNUSED_FIELD, new DetectUnusedFields(myClassNodes));
    }

    private List<MyClassNode> createClassNodes(List<String> classPaths) {
        List<MyClassNode> myNodes = new ArrayList<>();
        for (String path : classPaths) {
            Path p = Path.of(path);
            myNodes.add(creator.createMyClassNodeFromFile(p.toFile()));
        }
        return myNodes;
    }

    public List<Message> runSelectedChecks(Set<LintType> lintTypes) {
        Set<Message> messages = new HashSet<>();
        for (LintType lintType : lintTypes) {
            if (lintType == LintType.ADAPTER_PATTERN) {
                messages.addAll(checkTypeToCheck.get(lintType).run(myClassNodes.get(0)));
            } else {
                messages.addAll(runCheckOnAllNodes(lintType));
            }
        }
        return new ArrayList<>(messages);
    }


    private List<Message> runCheckOnAllNodes(LintType lintType) {
        Set<Message> messages = new HashSet<>();
        Check check = checkTypeToCheck.get(lintType);
        for (MyClassNode myClassNode : myClassNodes) {
            messages.addAll(check.run(myClassNode));
        }
        return new ArrayList<>(messages);
    }

    /**
     * NOTE: This method makes linter rely on ASM but is necessary to run Transformations
     * This is to reduce scope and not create adapters for all visitors
     *
     * @param transformations
     * @return
     */
    public List<Message> runSelectedTransformations(Set<LintType> transformations) {
        List<Message> allMessages = new ArrayList<>();
        for (LintType type : transformations) {
            Transformation transformation = transformationTypeToTransformation.get(type);
            List<Message> messages = transformation.run(myClassNodes);
            allMessages.addAll(messages);
        }
        return allMessages;
    }

}
