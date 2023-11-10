package domain.checks.PLKMockTestClasses;

public class IsReturned {
    public void method1() {
        PLKTestClass3 testClass3 = new PLKTestClass3();
        PLKTestClass1 testClass1 = testClass3.checkCreated();
        testClass1.checkReturned();
    }
}
