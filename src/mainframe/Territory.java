package mainframe;
import java.awt.Color;
import java.util.ArrayList;

public class Territory {
	
	int id;
	int owner_id;
	int[] true_color = new int[3];
	int resource_type; 	//diplomatic = 0, military = 1, economic = 2
	int resource_amt;
	int[] diplomatic_points = new int[9];
	int[] center;
	
	public Territory(int newid, int owner, int red, int green, int blue, int x, int y) {
		this(newid, owner, red, green, blue, x, y, 0, 0);
	}
	
	public Territory(int newid, int owner, int red, int green, int blue, int x, int y, int type, int amt) {
		id = newid;
		owner_id = owner;
		true_color[0] = red;
		true_color[1] = green;
		true_color[2] = blue;
		center = new int[]{x, y};
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
	
	public int getResourceType() {
		return resource_type;
	}
	
	public int getResourceAmt() {
		return resource_amt;
	}
	
	public void addDiplomaticPoints(int amount, int index) {
		diplomatic_points[index] += amount;
	}
	
	public void setDiplomaticPoints(int amount, int index) {
		diplomatic_points[index] = amount;
	}
	
	public int[] getDiplomaticPoints() {
		return diplomatic_points;
	}
	

}
