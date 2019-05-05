package mainframe;

public class Unit {
    private int location; //Index of territory
    private int target_location = -1; // Index of territory
    private int health;
    private int ownerId = -1;
    boolean supportMove = false;
    int moveStatus = 0; //0 = false, 1 = true, 2 = pending
    boolean victorious = false;

    public Unit(int newlocation, int newid){
        location = newlocation;
        health = 1;
        ownerId = newid;
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
    
    public void setSupportMove(boolean b) {
    	supportMove = b;
    }
    
    public boolean isSupporting() {
    	return supportMove;
    }
    
    public void setMoveStatus(int i) {
    	moveStatus = i;
    }
    
    public int getMoveStatus() {
    	return moveStatus;
    }
    
    public void setVictorious(boolean b) {
    	victorious = b;
    }
    
    public boolean getVictorious() {
    	return victorious;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    
    public int getOwnerId() {
    	return ownerId;
    }

    public void incrementHealth(int incr){health += incr;}
}
