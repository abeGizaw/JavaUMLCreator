package domain.kianascode;

import org.objectweb.asm.Label;

public class LocalVariableInfo {
    private final String name;
    private final Label firstLabel;
    private final Label lastLabel;
    private final int index;
    private boolean isInScope;

    public LocalVariableInfo(String name, Label firstLabel, Label lastLabel, int index) {
        this.name = name;
        this.firstLabel = firstLabel;
        this.lastLabel = lastLabel;
        this.index = index;
    }

    public void setIsInScope(boolean isInScope) {
        this.isInScope = isInScope;
    }

    public Label getFirstLabel() {
        return firstLabel;
    }

    public Label getLastLabel() {
        return lastLabel;
    }

    public int getIndex() {
        return index;
    }

    public boolean getIsInScope() {
        return isInScope;
    }
}
