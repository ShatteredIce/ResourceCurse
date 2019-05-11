package packets;

public class TerritoryInfo {

    int id = -1;
    int ownerId = -1; 
	int[] diplomatic_points;

	
	public TerritoryInfo(){
		
	}
	
	public TerritoryInfo(int newid, int newownerid, int[] newdiplo){
		id = newid;
		ownerId = newownerid;
		diplomatic_points = newdiplo;
	}
	
	public int getId() {
		return id;
	}
	
	public int getOwnerId(){
		return ownerId;
	}
	
	public int[] getDiplomaticPoints(){
		return diplomatic_points;
	}
	
}