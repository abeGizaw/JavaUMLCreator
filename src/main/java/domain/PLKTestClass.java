package domain;

public class PLKTestClass {
    private ConcreteClass1 concreteClass1;

    public void method1(ConcreteClass3 concreteClass3) {
        // field
        concreteClass1.method1();

        // created object
        ConcreteClass2 concreteClass2 = new ConcreteClass2();
        concreteClass2.method1();

        // parameter
        concreteClass3.method1();

        // this object
        this.method2();
    }

    public void method2() {

    }
}
