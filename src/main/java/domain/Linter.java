package domain;

import domain.checks.*;
import domain.myasm.MyASMClassNode;
import domain.transformations.DeleteUnusedFields;
import domain.transformations.Transformation;
import org.objectweb.asm.tree.ClassNode;

import java.nio.file.Path;
import java.util.*;

public class Linter {
    private final MyClassNodeCreator creator;
    private final List<MyClassNode> myClassNodes;
    private final Map<CheckType, Check> checkTypeToCheck;
    private final Map<TransformationType, Transformation> transformationTypeToTransformation;

    public Linter(List<String> classPaths, MyClassNodeCreator myClassNodeCreator) {
        this.creator = myClassNodeCreator;
        this.myClassNodes = createClassNodes(classPaths);
        this.checkTypeToCheck = new HashMap<>();
        this.transformationTypeToTransformation = new HashMap<>();
    }

    private List<MyClassNode> createClassNodes(List<String> classPaths) {
        List<MyClassNode> myNodes = new ArrayList<>();
        for (String path : classPaths) {
            Path p = Path.of(path);
            myNodes.add(creator.createMyClassNodeFromFile(p.toFile()));
        }
        return myNodes;
    }

    public List<Message> runSelectedChecks(Set<CheckType> checkTypes) {
        createSelectedChecks(checkTypes);
        List<Message> messages = new ArrayList<>();
        for (CheckType checkType : checkTypes) {
            if (checkType == CheckType.ADAPTER_PATTERN) {
                messages.addAll(checkTypeToCheck.get(checkType).run(myClassNodes.get(0)));
            } else {
                messages.addAll(runCheckOnAllNodes(checkType));
            }
        }
        return messages;
    }

    private void createSelectedChecks(Set<CheckType> checkTypes) {
        for (CheckType checkType : checkTypes) {
            if (checkType == CheckType.FINAL_LOCAL_VARIABLES) {
                checkTypeToCheck.put(CheckType.FINAL_LOCAL_VARIABLES, new FinalLocalVariables());
            } else if (checkType == CheckType.HIDDEN_FIELDS) {
                checkTypeToCheck.put(CheckType.HIDDEN_FIELDS, new HiddenFields());
            } else if (checkType == CheckType.NAMING_CONVENTION) {
                checkTypeToCheck.put(CheckType.NAMING_CONVENTION, new NamingConventionCheck());
            } else if (checkType == CheckType.COMPOSITION_OVER_INHERITANCE) {
                checkTypeToCheck.put(CheckType.COMPOSITION_OVER_INHERITANCE, new CompositionOverInheritance());
            } else if (checkType == CheckType.INTERFACE_OVER_IMPLEMENTATION) {
                checkTypeToCheck.put(CheckType.INTERFACE_OVER_IMPLEMENTATION, new InterfaceOverImplementation());
            } else if (checkType == CheckType.PLK) {
                checkTypeToCheck.put(CheckType.PLK, new PrincipleOfLeastKnowledge());
            } else if (checkType == CheckType.ADAPTER_PATTERN) {
                checkTypeToCheck.put(CheckType.ADAPTER_PATTERN, new AdapterPattern(myClassNodes));
            } else if (checkType == CheckType.STRATEGY_PATTERN) {
                checkTypeToCheck.put(CheckType.STRATEGY_PATTERN, new StrategyPattern(creator));
            } else if (checkType == CheckType.TEMPLATE_METHOD_PATTERN) {
                checkTypeToCheck.put(CheckType.TEMPLATE_METHOD_PATTERN, new TemplateMethodPattern());
            }
        }
    }

    private List<Message> runCheckOnAllNodes(CheckType checkType) {
        List<Message> messages = new ArrayList<>();
        Check check = checkTypeToCheck.get(checkType);
        for (MyClassNode myClassNode : myClassNodes) {
            messages.addAll(check.run(myClassNode));
        }
        return messages;
    }

    /**
     * NOTE: This method makes linter rely on ASM but is necessary to run Transformations
     * This is to reduce scope and not create adapters for all visitors
      * @param transformations
     * @param outputPath
     */
    public void runSelectedTransformations(Set<TransformationType> transformations, String outputPath) {
        createSelectedTransformation(transformations, outputPath);
        for(TransformationType type: transformations){
            Transformation transformation = transformationTypeToTransformation.get(type);
            List<ClassNode> classNodes = new ArrayList<>();
            for(MyClassNode myClassNode : myClassNodes){
                MyASMClassNode asmClassNode = (MyASMClassNode) myClassNode;
                classNodes.add(asmClassNode.getClassNode());
            }
            transformation.run(classNodes);

        }
    }

    private void createSelectedTransformation(Set<TransformationType> transformations, String outputPath) {
        for(TransformationType type: transformations){
            System.out.println("TYPE");
            System.out.println(type);
            switch (type){
                case REMOVE_UNUSED_FIELDS:
                    DetectUnusedFields detectUnusedFields = new DetectUnusedFields(myClassNodes);
                    detectUnusedFields.run(null);
                    transformationTypeToTransformation.put(TransformationType.REMOVE_UNUSED_FIELDS, new DeleteUnusedFields(outputPath, detectUnusedFields.getNamesToDelete()));
                default:
                    break;
            }
        }
    }
}
