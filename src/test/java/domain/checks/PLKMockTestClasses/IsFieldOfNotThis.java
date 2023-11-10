package domain.checks.PLKMockTestClasses;

public class IsFieldOfNotThis {
    private PLKTestClass1 testClass1;

    public void method1() {
        testClass1.testClass2.checkNotField();
    }
}
