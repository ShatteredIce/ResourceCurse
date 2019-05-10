package packets;

import java.util.ArrayList;

import mainframe.Unit;

public class PlayerInfo {
	
	private int action; //0 = create, 1 = destroy
	private float[] color;
	private int id;
	
	
	public PlayerInfo(){
		
	}
	
	public PlayerInfo(int newaction, float[] newcolor, int newid){
		action = newaction;
		color = newcolor;
		id = newid;
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

}
