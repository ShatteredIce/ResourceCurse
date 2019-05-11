package rendering;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import mainframe.GameException;

public class GameTextures {
	
	final static Texture unittex = new Texture("unit.png");
	final static Texture selectedunittex = new Texture("selectedunit.png");
	final static Texture movearrowtex = new Texture("movearrow.png");
	final static Texture supportarrowtex = new Texture("supportarrow.png");
	final static Texture zero = new Texture("0.png");
	final static Texture one = new Texture("1.png");
	final static Texture two = new Texture("2.png");
	final static Texture three = new Texture("3.png");
	final static Texture four = new Texture("4.png");
	final static Texture five = new Texture("5.png");
	final static Texture six = new Texture("6.png");
	final static Texture seven = new Texture("7.png");
	final static Texture eight = new Texture("8.png");
	final static Texture nine = new Texture("9.png");
	
	
	//bind texture corresponding to id
	public void loadTexture(int id){
		switch (id) {
		case -1:
			glBindTexture(GL_TEXTURE_2D, 0);
		case 0:
			unittex.bind(0);
			break;
		case 1:
			selectedunittex.bind(0);
			break;
		case 2:
			movearrowtex.bind(0);
			break;
		case 3:
			supportarrowtex.bind(0);
			break;
		case 4:
			zero.bind(0);
			break;
		case 5:
			one.bind(0);
			break;
		case 6:
			two.bind(0);
			break;
		case 7:
			three.bind(0);
			break;
		case 8:
			four.bind(0);
			break;
		case 9:
			five.bind(0);
			break;
		case 10:
			six.bind(0);
			break;
		case 11:
			seven.bind(0);
			break;
		case 12:
			eight.bind(0);
			break;
		case 13:
			nine.bind(0);
			break;
		default:
			try {
				throw new GameException("Requested Texture ID does not exist");
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}

}