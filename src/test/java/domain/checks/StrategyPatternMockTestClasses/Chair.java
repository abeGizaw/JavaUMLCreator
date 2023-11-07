package domain.checks.StrategyPatternMockTestClasses;

public class Chair implements Furniture {
    final int numLegs;
    static final boolean leatherMaterail = false;
    static final boolean CLOTH = true;
    String wood;

    Chair (String woodType){
        wood = woodType;
        numLegs = 4;

        System.out.println("This is a chair with " + numLegs + "legs and has wood " + wood);
    }

    @Override
    public void sit() {
        System.out.println("Sitting");
    }

    private void STandIng (){}
}
