package mainframe;

import java.util.ArrayList;

public class Player {
	
	int id;

	//resources
	int oil = 0;
	int steel = 0;
	int food = 0;
	
	ArrayList<Unit> units;

	public Player(int newid) {
		id = newid;
		units = new ArrayList<>();
	}

}
