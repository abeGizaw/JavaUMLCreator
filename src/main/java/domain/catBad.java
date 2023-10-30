package domain;

public class catBad extends Animal{

    private String Ability;
    private int toys;
    private static final String food = "Purina";

    public static final String BED  = "fluffy";

    private final String var = "help";
    private final String BADVAR = "table";
    private Chair chair;

    Furniture furniture;
    Furniture strategyFurniture;

    catBad(String name, int age, String breed, Chair c){
        super(name, age, breed);
        Ability =  "MEEOOOW";
        toys = 4;
        chair = c;
        HasBirthday();
        furniture = new Chair("oak");
    }

    public int HasBirthday(){
        age += 1;
        return age;

    }

    public String getName(){
        return name;
    }

    @Override
    public void doSomething() {
        System.out.println(specialAbility());
    }

    private String specialAbility(){

        return Ability + food;
    }

    public int addToy(){
        toys++;
        System.out.println("Total toys: " + toys);
        return toys;
    }
    public void nothing(){}

    public void setFurnitureStrat(Furniture f){
        strategyFurniture = f;

    }

}
