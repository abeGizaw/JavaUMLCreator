package domain.checks.FinalLocalVariablesMockTestClasses;

public class MultipleScopes {
    public int method1() {
        int x = 1;
        x = 2;
        int y = 3;
        int z = 5;
        int w = 1032;
        if (x == 2) {
            int a = 1;
            int b = 2;
            a = 3;
            y = 2;
        }
        int c = 142;
        int d = 384;
        c = 129834;
        return x;
    }
}
