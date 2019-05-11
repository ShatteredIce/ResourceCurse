package mainframe;

public class Unit {
    private int location; //Index of territory
    private int target_location = -1; // Index of territory
    private int ownerId = -1;
    boolean supportMove = false;
    int moveStatus = 0; //0 = false, 1 = true, 2 = pending
    int destroyedTerritoryIndex= -1;

    public Unit(int newlocation, int newid){
        location = newlocation;
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
    
    public void setDestroyedTerritoryIndex(int i) {
    	destroyedTerritoryIndex = i;
    }
    
    public int getDestroyedTerritoryIndex() {
    	return destroyedTerritoryIndex;
    }
    
    public int getOwnerId() {
    	return ownerId;
    }
}
