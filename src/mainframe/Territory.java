package mainframe;

import  java.lang.Math;

public class Territory {
	
	int id;
	int owner_id;
	int influenced_by;
	int[] true_color = new int[3];
	int resource_type; 	//diplomatic = 0, military = 1, economic = 2
	int resource_amt;
	int[] diplomatic_points = new int[9];
	int economicPoints;
	int[] center;
	Unit occupying = null;
	int adjacent[];
	
	public Territory(int newid, int owner, int red, int green, int blue, int x, int y) {
		this(newid, owner, red, green, blue, x, y, 0, 0, null);
	}
	
	public Territory(int newid, int owner, int red, int green, int blue, int x, int y, int type, int amt, int adjacent[]) {
		id = newid;
		owner_id = owner;
		influenced_by = 0;
		true_color[0] = red;
		true_color[1] = green;
		true_color[2] = blue;
		center = new int[]{x, y};
		resource_type = type;
		resource_amt = amt;
		this.adjacent = new int[adjacent.length];
		for(int i = 0; i < adjacent.length; i++){
		    this.adjacent[i] = adjacent[i];
        }
	}
	
	public boolean checkTrueColor(int red, int green, int blue) {
		if(true_color[0] == red && true_color[1] == green && true_color[2] == blue) {
			return true;
		}
//		System.out.println(red+","+green+","+blue);
		return false;
	}
	
	public int getId() {
		return id;
	}
	
	public void setOwner(int newid) {
		owner_id = newid;
	}
	
	public int getOwner() {
		return owner_id;
	}
	
	public int getResourceType() {
		return resource_type;
	}
	
	public int getResourceAmt() {
		return resource_amt;
	}
	
	public void addDiplomaticPoints(int amount, int index) {
	    if(owner_id == 0) {
            diplomatic_points[index] += amount;
        }
	}
	
	public void updateInfluence() {
		for (int i = 0; i < diplomatic_points.length; i++) {
			if(diplomatic_points[i] >= 20) {
				owner_id = i;
				influenced_by = 0;
				break;
			}
			else {
				boolean greaterThan = true;
				for (int j = 0; j < diplomatic_points.length; j++) {
					if(i != j && diplomatic_points[i] - diplomatic_points[j] < 5) {
						greaterThan = false;
					}
				}
				if(greaterThan) {
					influenced_by = i;
					break;
				}
			}
			if(i == diplomatic_points.length - 1) {
				influenced_by = 0;
			}
		}
	}
	
	public void setDiplomaticPoints(int amount, int index) {
		diplomatic_points[index] = amount;
	}
	
	public void setDiplomaticPoints(int[] arr) {
		diplomatic_points = arr;
	}
	
	public int[] getDiplomaticPoints() {
		return diplomatic_points;
	}

	public void addEconomicPoints(int amt){
	    economicPoints += amt;
        resource_amt = (int)Math.floor(3.0*Math.log10(economicPoints)) > 1 ? (int)Math.floor(3.0*Math.log10(economicPoints)) : 1;
    }
	
	public void setOccupyingUnit(Unit u) {
		occupying = u;
	}
	
	public Unit getOccupyingUnit() {
		return occupying;
	}

	public int getInfluenced_by(){return influenced_by;}
	

}
