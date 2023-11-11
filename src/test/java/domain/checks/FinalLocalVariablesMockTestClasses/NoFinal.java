package domain.checks.FinalLocalVariablesMockTestClasses;

public class NoFinal {
    public void method1(int x) {
        x = 3;
        int y = 2;
        y = 1;
        while (x == 3) {
            int z = 0;
            x = 2;
            z = 1;
        }
    }
}
