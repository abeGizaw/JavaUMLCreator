package domain.kianascode;

public class PLKTestClass {
    private PLKConcreteClass1 concreteClass1;

    public void testAllTypes(PLKConcreteClass3 concreteClass3) {
        // field
        concreteClass1.method1();
        concreteClass1.concreteClass3.method1();

        // created object
        PLKConcreteClass2 concreteClass2 = new PLKConcreteClass2(concreteClass1);
        PLKConcreteClass1 concreteClass1a = concreteClass2.method1();

        // parameter
        concreteClass3.methodWith3Arguments(1, concreteClass2.concreteClass1, concreteClass1);
        concreteClass1a.method1();

        // this object
        this.method1();
    }

    private void method1() {

    }
}
