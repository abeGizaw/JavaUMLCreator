package domain;

public class Chair implements Furniture{
    final int numLegs;
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
}
