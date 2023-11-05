package domain;

public class PLKTestClass {
    private ConcreteClass1 concreteClass1;
//    private ConcreteClass1 concreteClass1a;

    public void testAllTypes(ConcreteClass3 concreteClass3) {
        // field
        concreteClass1.method1();
        concreteClass1.concreteClass3.method1();

        // created object
        ConcreteClass2 concreteClass2 = new ConcreteClass2(concreteClass1);
        ConcreteClass1 concreteClass1a = concreteClass2.method1();

        // parameter
        concreteClass3.methodWith3Arguments(1, concreteClass2.concreteClass1, concreteClass1);
        concreteClass1a.method1();

        // this object
        this.method1();
    }

    private void method1() {

    }
}
