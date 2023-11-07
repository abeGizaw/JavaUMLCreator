package domain;

public class LocalVariableInfo {
    private final String name;
    private final MyLabel firstLabel;
    private final MyLabel lastLabel;
    private final int index;
    private boolean isInScope;
    private boolean hasBeenStored;
    private boolean hasBeenStoredOnce;

    public LocalVariableInfo(String name, MyLabel firstLabel, MyLabel lastLabel, int index) {
        this.name = name;
        this.firstLabel = firstLabel;
        this.lastLabel = lastLabel;
        this.index = index;
    }

    public void setIsInScope(boolean isInScope) {
        this.isInScope = isInScope;
    }

    public void setHasBeenStored(boolean hasBeenStored) {
        this.hasBeenStored = hasBeenStored;
    }

    public void setHasBeenStoredOnce(boolean hasBeenStoredOnce) {
        this.hasBeenStoredOnce = hasBeenStoredOnce;
    }

    public String getName() {
        return name;
    }

    public MyLabel getFirstLabel() {
        return firstLabel;
    }

    public MyLabel getLastLabel() {
        return lastLabel;
    }

    public int getIndex() {
        return index;
    }

    public boolean getIsInScope() {
        return isInScope;
    }

    public boolean getHasBeenStored() {
        return hasBeenStored;
    }

    public boolean getHasBeenStoredOnce() {
        return hasBeenStoredOnce;
    }
}
