package domain.checks.PLKMockTestClasses;

public class IsParameterAndHasArguments {
    PLKTestClass1 testClass1;

    public void method1(PLKTestClass4 testClass4) {
        testClass4.checkParameterWithArguments(1, testClass1.testClass2, testClass1);
    }
}
