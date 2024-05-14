public abstract class Algorithm {
    public abstract Adaptee runAlgorithm();

    public final void runTemplate() {
        method1();
        method2();
    }

    protected void method1() {

    }

    protected abstract void method2();
}
