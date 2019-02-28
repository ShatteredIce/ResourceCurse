package mainframe;

public class Unit {
    int location; //Index of territoy
    int health;

    public Unit(int location){
        this.location = location;
        health = 100;
    }

    public void setLocation(int location){
        this.location = location;
    }

    public int getLocation(){
        return location;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
