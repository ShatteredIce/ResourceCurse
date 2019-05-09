package packets;

public class MouseClick {
	
	private int playerId;
	private int button;
	private int action;
	private double[] x;
	private double[] y;
	
	public MouseClick() {
		
	}
	
	public MouseClick(int newbutton, int newaction, double[] newx, double[] newy, int newid) {
		button = newbutton;
		action = newaction;
		x = newx;
		y = newy;
		playerId = newid;

	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public int getButton() {
		return button;
	}
	
	public int getAction() {
		return action;
	}
	
	public double[] getX() {
		return x;
	}
	
	public double[] getY() {
		return y;
	}
	
}
