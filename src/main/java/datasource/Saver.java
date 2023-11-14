package datasource;

public interface Saver {
    void saveMessage(String message);
    public void writeToFile(String info, String fileType, String outputPath);
}
