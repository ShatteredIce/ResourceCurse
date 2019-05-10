package packets;

public class UnitInfo {

    int ownerId = -1;
    int location = -1; //Index of territory
    int target_location = -1;
    boolean supportMove = false;
	
	public UnitInfo(){
		
	}
	
	public UnitInfo(int newownerid, int newlocation){
		ownerId = newownerid;
		location = newlocation;
	}
	
	public UnitInfo(int newownerid, int newlocation, int newtarget, boolean supporting){
		ownerId = newownerid;
		location = newlocation;
		target_location = newtarget;
		supportMove = supporting;
	}
	
	
	public int getOwnerId(){
		return ownerId;
	}
	
	public int getLocation(){
		return location;
	}
	
	 public void setTarget(int newlocation){
        target_location = newlocation;
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

}
