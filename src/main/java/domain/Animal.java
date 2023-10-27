package domain;

public abstract class Animal {

    protected String name;

    protected int age;

    protected String breed;

    Animal(String name, int age, String breed){
        this.name = name;
        this.age = age;
        this.breed = breed;
    }

    public String getName(){
        return name;
    }
    public String getBreed(){return breed;}
    public  int getAge(){return age;}
    public abstract void doSomething();
}
