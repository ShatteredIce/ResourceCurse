package mainframe;

import java.util.ArrayList;

public class Player {
	
	int id;
	
	float[] color;

	//resources
	int oil = 0;
	int steel = 0;
	int food = 0;

	ArrayList<Unit> units;

	
	public Player(int newid, float red, float green, float blue) {
		id = newid;
		color = new float[]{red, green, blue};
		units = new ArrayList<Unit>();
	}
	
	public float[] getColor() {
		return color;
	}

}
