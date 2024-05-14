import java.util.ArrayList;

public class Client {
    private Target target;
    private Algorithm algorithm;
    ArrayList<String> implementation = new ArrayList<>();

    public Client(Algorithm algorithm) {
        target = new Adapter();
        this.algorithm = algorithm;
    }

    public void method1() {
        Adaptee Adaptee = algorithm.runAlgorithm();
        Adaptee.method();
    }
}