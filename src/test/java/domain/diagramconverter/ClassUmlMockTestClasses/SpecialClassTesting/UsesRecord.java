package domain.diagramconverter.ClassUmlMockTestClasses.SpecialClassTesting;

import domain.diagramconverter.RelationsManager;

public class UsesRecord {
    private final MyRecord record = new MyRecord(1, "yes", new RelationsManager());

    public UsesRecord(){
        int test = this.record.time();
    }

    public void removeYellowLines(){
        int what = this.record.time() + 1;
    }

}
