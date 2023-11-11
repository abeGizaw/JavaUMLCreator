package datasource;

public interface Exporter {
    public void save(String outputPath, String className, byte[] bytecode);

}
