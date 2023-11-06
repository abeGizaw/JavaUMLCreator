package domain;

import org.objectweb.asm.Label;

public class MyASMLabel extends MyLabel {
    private Label label;

    public MyASMLabel(Label label) {
        this.label = label;
    }
}
