package rendering;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import mainframe.GameException;

public class GameTextures {
	
	final static Texture unittex = new Texture("unit.png");
	final static Texture selectedunittex = new Texture("selectedunit.png");
	final static Texture movearrowtex = new Texture("movearrow.png");
	final static Texture supportarrowtex = new Texture("supportarrow.png");
	
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
		default:
			try {
				throw new GameException("Requested Texture ID does not exist");
			} catch (GameException e) {
				e.printStackTrace();
			}
		}
	}

}