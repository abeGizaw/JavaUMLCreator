package domain.kianascode;

public class PLKTestClass {
    private PLKConcreteClass1 concreteClass1;

    public void testAllTypes(PLKConcreteClass3 concreteClass3) {
        // field
        concreteClass1.checkField();
        concreteClass1.concreteClass3.checkNotField();

        // created object
        PLKConcreteClass2 concreteClass2 = new PLKConcreteClass2(concreteClass1);
        PLKConcreteClass1 concreteClass1a = concreteClass2.checkCreated();

        // parameter
        concreteClass3.checkParameterWithArguments(1, concreteClass2.concreteClass1, concreteClass1);
        concreteClass1a.checkNotParameter();

        // this object
        this.checkThis();
    }

    private void checkThis() {

    }
}
