package domain.kianascode;

public class FinalLocalVariablesTestClass {
    public int testMethod() {
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

    public void testMethod2() {
        int x = 1;
        x = 2;
        final int y = 3;
    }

    public void testMethod3() {
        int x = 1;
        x = 2;
        final int y = 3;
        while (x == 2) {
            int z = 3214;
            x = 1;
        }
    }
}
