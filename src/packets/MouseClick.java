package packets;

public class MouseClick {
	
	private int playerId;
	private int action;
	private int territoryId;
	
	public MouseClick() {
		
	}
	
	public MouseClick(int newid, int newaction,  int newterritoryId) {
		playerId = newid;
		action = newaction;
		territoryId = newterritoryId;

	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public int getTerritoryId() {
		return territoryId;
	}
	
	public int getAction() {
		return action;
	}
	
}
