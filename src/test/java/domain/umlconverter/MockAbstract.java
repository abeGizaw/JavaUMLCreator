package domain.umlconverter;

public abstract class MockAbstract {

    private String model;
    private int year;

    public MockAbstract(String model, int year) {
        this.model = model;
        this.year = year;
    }

    public abstract void startEngine();

    public abstract void stopEngine();

    public void honk() {
        System.out.println("Honk! Honk!");
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
