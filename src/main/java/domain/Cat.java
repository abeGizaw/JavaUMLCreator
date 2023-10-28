package domain;

public class Cat extends Animal{

    private String Ability;
    private int toys;
    private final String food = "Purina";

    public final String BED  = "fluffy";
    private Chair chair;

    Furniture furniture;

    Cat (String name, int age, String breed, Chair c){
        super(name, age, breed);
        Ability =  "MEEOOOW";
        toys = 4;
        chair = c;
        HasBirthday();
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

}
