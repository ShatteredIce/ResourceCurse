package packets;

import java.util.ArrayList;

import mainframe.Unit;

public class PlayerInfo {
	
	private int action; //0 = create, 1 = destroy
	private float[] color;
	private int id;
	
	private ArrayList<UnitInfo> units;

	
	public PlayerInfo(){
		
	}
	
	public PlayerInfo(int newaction, float[] newcolor, int newid){
		action = newaction;
		color = newcolor;
		id = newid;
		units = new ArrayList<>();
	}

	public int getAction() {
		return action;
	}

	public float[] getColor() {
		return color;
	}

	public int getId() {
		return id;
	}
	
	public void setUnits(ArrayList<UnitInfo> myunits) {
		units = myunits;
	}
	
	public ArrayList<UnitInfo> getUnits(){
		return units;
	}

}
