package domain;

import domain.checks.*;
import domain.myasm.MyASMClassNode;
import domain.transformations.DeleteUnusedFields;
import domain.transformations.Transformation;
import org.objectweb.asm.tree.ClassNode;

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
        DetectUnusedFields detectUnusedFields = new DetectUnusedFields(myClassNodes);
        detectUnusedFields.run(null);
        transformationTypeToTransformation.put(LintType.UNUSED_FIELD, new DeleteUnusedFields(outputPath, detectUnusedFields.getNamesToDelete()));
    }

    private void populateCheckMap() {
        checkTypeToCheck.put(LintType.FINAL_LOCAL_VARIABLES, new FinalLocalVariables());
        checkTypeToCheck.put(LintType.HIDDEN_FIELDS, new FieldHiding());
        checkTypeToCheck.put(LintType.NAMING_CONVENTION, new NamingConventionCheck());
        checkTypeToCheck.put(LintType.COMPOSITION_OVER_INHERITANCE, new CompositionOverInheritance());
        checkTypeToCheck.put(LintType.INTERFACE_OVER_IMPLEMENTATION, new InterfaceOverImplementation());
        checkTypeToCheck.put(LintType.PLK, new PrincipleOfLeastKnowledge());
        checkTypeToCheck.put(LintType.ADAPTER_PATTERN, new AdapterPattern(myClassNodes));
        checkTypeToCheck.put(LintType.STRATEGY_PATTERN, new StrategyPattern(creator));
        checkTypeToCheck.put(LintType.TEMPLATE_METHOD_PATTERN, new TemplateMethodPattern());
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
        List<Message> messages = new ArrayList<>();
        for (LintType lintType : lintTypes) {
            if (lintType == LintType.ADAPTER_PATTERN) {
                messages.addAll(checkTypeToCheck.get(lintType).run(myClassNodes.get(0)));
            } else {
                messages.addAll(runCheckOnAllNodes(lintType));
            }
        }
        return messages;
    }



    private List<Message> runCheckOnAllNodes(LintType lintType) {
        List<Message> messages = new ArrayList<>();
        Check check = checkTypeToCheck.get(lintType);
        for (MyClassNode myClassNode : myClassNodes) {
            messages.addAll(check.run(myClassNode));
        }
        return messages;
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
        for(LintType type: transformations){
            Transformation transformation = transformationTypeToTransformation.get(type);
            List<ClassNode> classNodes = new ArrayList<>();
            for(MyClassNode myClassNode : myClassNodes){
                MyASMClassNode asmClassNode = (MyASMClassNode) myClassNode;
                classNodes.add(asmClassNode.getClassNode());
            }
            List<Message> messages = transformation.run(classNodes);
            allMessages.addAll(messages);

        }
        return allMessages;
    }

//    private void createSelectedTransformation(Set<LintType> transformations, String outputPath) {
//        for(LintType type: transformations){
//            System.out.println("TYPE");
//            System.out.println(type);
//            switch (type){
//                case UNUSED_FIELD:
//                    DetectUnusedFields detectUnusedFields = new DetectUnusedFields(myClassNodes);
//                    detectUnusedFields.run(null);
//                    transformationTypeToTransformation.put(LintType.UNUSED_FIELD, new DeleteUnusedFields(outputPath, detectUnusedFields.getNamesToDelete()));
//                default:
//                    break;
//            }
//       }
//    }
}
