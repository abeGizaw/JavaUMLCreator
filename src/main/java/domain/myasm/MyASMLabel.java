package domain.myasm;

import domain.MyLabel;
import org.objectweb.asm.Label;

public class MyASMLabel implements MyLabel {
    private final Label label;

    public MyASMLabel(Label label) {
        this.label = label;
    }

    @Override
    public boolean equals(MyLabel otherLabel) {
        if (!(otherLabel instanceof MyASMLabel)) {
            return false;
        }
        return this.label.equals(((MyASMLabel) otherLabel).getLabel());
    }

    public Label getLabel() {
        return label;
    }
}
