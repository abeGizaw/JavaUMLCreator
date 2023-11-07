package domain.kianascode;

public class PLKConcreteClass2 {
    public PLKConcreteClass1 concreteClass1;

    public PLKConcreteClass2(PLKConcreteClass1 concreteClass1) {
        this.concreteClass1 = concreteClass1;
    }

    public PLKConcreteClass1 method1() {
        return new PLKConcreteClass1();
    }
}
