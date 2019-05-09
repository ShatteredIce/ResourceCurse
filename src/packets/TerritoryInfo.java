package packets;

public class TerritoryInfo {

    int id = -1;
    int ownerId = -1; 

	
	public TerritoryInfo(){
		
	}
	
	public TerritoryInfo(int newid, int newownerid){
		id = newid;
		ownerId = newownerid;
	}
	
	public int getId() {
		return id;
	}
	
	public int getOwnerId(){
		return ownerId;
	}
	
}