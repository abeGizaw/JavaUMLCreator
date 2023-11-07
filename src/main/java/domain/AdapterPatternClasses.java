package domain;

public class AdapterPatternClasses {
    private String adapter;
    private String target;
    private String adaptee;
    private String client;

    public AdapterPatternClasses(String adapter, String target, String adaptee, String client) {
        this.adapter = adapter;
        this.target = target;
        this.adaptee = adaptee;
        this.client = client;
    }

    public AdapterPatternClasses(String adapter, String target, String adaptee) {
        this(adapter, target, adaptee, null);
    }

    public String getAdapter() {
        return adapter;
    }

    public String getTarget() {
        return target;
    }

    public String getAdaptee() {
        return adaptee;
    }

    public String getClient() {
        return client;
    }
}
