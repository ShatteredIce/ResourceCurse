package mainframe;

public class Unit {
    private int location; //Index of territory
    private int target_location = -1; // Index of territory
    private int health;

    public Unit(int location){
        this.location = location;
        health = 1;
    }

    public void setLocation(int location){
        this.location = location;
    }
    
    public void setTarget(int location){
        this.target_location = location;
    }

    public int getLocation(){
        return location;
    }
    
    public int getTarget() {
    	return target_location;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void incrementHealth(int incr){health += incr;}
}
