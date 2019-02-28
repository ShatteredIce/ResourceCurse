package mainframe;
import java.awt.Color;

public class Territory {
	
	int id;
	int owner_id;
	int numUnits;
	int[] true_color = new int[3];
	int resource_type; 	//oil = 0, steel = 1, food = 2
	int resource_amt;
	
	public Territory(int newid, int owner, int red, int green, int blue) {
		this(newid, owner, red, green, blue, 0, 0);
	}
	
	public Territory(int newid, int owner, int red, int green, int blue, int type, int amt) {
		id = newid;
		owner_id = owner;
		true_color[0] = red;
		true_color[1] = green;
		true_color[2] = blue;
		numUnits = 0;
		resource_type = type;
		resource_amt = amt;
	}
	
	public boolean checkTrueColor(int red, int green, int blue) {
		if(true_color[0] == red && true_color[1] == green && true_color[2] == blue) {
			return true;
		}
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

	public int getNumUnits(){ return  numUnits;}

	public void setNumUnits(int numUnits){ this.numUnits = numUnits;}

	public  void incrementNumUnits(int incr){numUnits += incr;}

	
	public int getResourceType() {
		return resource_type;
	}
	
	public int getResourceAmt() {
		return resource_amt;
	}

}
