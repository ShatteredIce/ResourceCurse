package mainframe;

import java.util.ArrayList;

public class Player {
	
	private int id;
	private float[] color;

	private ArrayList<Unit> units;

	int[] resources = new int[] {0, 0, 0};
	//oil = 0, steel = 1, food = 2
		
	public Player(int newid, float red, float green, float blue) {
		id = newid;
		color = new float[]{red, green, blue};
		units = new ArrayList<Unit>();
	}
	
	public float[] getColor() {
		return color;
	}
	
	public void addResource(int type, int amount) {
		resources[type] += amount;
	}
	
	public void subResource(int type, int amount) {
		resources[type] -= amount;
	}
	
	public void setResources(int type, int amount) {
		resources[type] = amount;
	}
	
	public int checkResource(int type) {
		return resources[type];
	}
	
	public int[] getResources() {
		return resources;
	}
	
	public int getId() {
		return id;
	}
	
	public void addUnit(Unit u){
		units.add(u);
	}
	
	public ArrayList<Unit> getUnits(){
		return units;
	}
	
	public int numUnits() {
		return units.size();
	}

}
