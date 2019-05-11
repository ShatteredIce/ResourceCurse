package packets;

public class PointDeployments {
	
	private int playerId;
	private int[] pointDeployments;
	private int pointsSaved;
	
	public PointDeployments() {
		
	}
	
	public PointDeployments(int newid, int[] newDeployments) {
		playerId = newid;
		pointDeployments = newDeployments;
		pointsSaved = 0;
	}
	
	public PointDeployments(int newid, int[] newDeployments, int points) {
		playerId = newid;
		pointDeployments = newDeployments;
		pointsSaved = points;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public int[] getDeployments() {
		return pointDeployments;
	}
	
	public int getPointsSaved() {
		return pointsSaved;
	}

}
