package mainframe;
import java.awt.Color;

public class Territory {
	
	int id;
	int owner_id;
	int numUnits;
	int[] true_color = new int[3];
	
	public Territory(int newid, int owner, int red, int green, int blue) {
		id = newid;
		owner_id = owner;
		true_color[0] = red;
		true_color[1] = green;
		true_color[2] = blue;
		numUnits = 0;
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
	
	public int getOwner() {
		return owner_id;
	}

	public int getNumUnits(){ return  numUnits;}

	public void setNumUnits(int numUnits){ this.numUnits = numUnits;}

	public  void incrementNumUnits(int incr){numUnits += incr;}


}
