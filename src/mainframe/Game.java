package mainframe;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import rendering.Model;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {

	//Rendering variables
	final double[] textureCoords = {0, 0, 0, 1, 1, 0, 1, 1};
	final int[] indices = {0, 1, 2, 2, 1, 3};
	final double[] placeholder = {0, 0, 0, 0, 0, 0, 0, 0};
	Model model;
	
	GameEngine lwjgl3 = new GameEngine(this);
	
	Map gamemap;

	Player[] players;

	int territorySelected;

	public void run() {
		lwjgl3.create();
		start();
		lwjgl3.destroy();
	}

	private void start() {
		
		lwjgl3.setup();
		
		model = new Model(placeholder, textureCoords, indices);
		gamemap = new TestMap();
		players = new Player[2];
		territorySelected = -1;

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(lwjgl3.getWindowHandle()) ) {			
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			gameLoop();

			glfwSwapBuffers(lwjgl3.getWindowHandle()); // swap the color buffers
		}
	}
	
	public void gameLoop() {
		lwjgl3.projectRelativeCameraCoordinates();
		gamemap.getTexture().bind();
		model.render(0, 0, lwjgl3.getWindowWidth(), lwjgl3.getWindowHeight());
	}
	
	public void onMouseClick(int button, int action, DoubleBuffer xpos, DoubleBuffer ypos) {
		if ( button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			System.out.println("Left Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
			int t_id = gamemap.getTerritoryClicked((int) xpos.get(0), (int) ypos.get(0));
			if(t_id != -1) {
				if(territorySelected != -1){
					if(territorySelected != t_id) {
						gamemap.territories.get(t_id).incrementNumUnits(gamemap.territories.get(territorySelected).getNumUnits());
						gamemap.territories.get(territorySelected).setNumUnits(0);
						for(Unit unit: players[0].units){
							if(unit.getLocation() == territorySelected){
								unit.setLocation(t_id);
							}
						}
						System.out.println("Territory Clicked: " + t_id);
						System.out.println("Units: " + gamemap.territories.get(t_id).getNumUnits());
					}
					territorySelected = -1;
				}else {
					players[0].units.add(new Unit(t_id));
					gamemap.territories.get(t_id).incrementNumUnits(1);
					System.out.println("Territory Clicked: " + t_id);
					System.out.println("Units: " + gamemap.territories.get(t_id).getNumUnits());
				}
			}
		}
		else if( button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
			System.out.println("Right Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
			territorySelected = gamemap.getTerritoryClicked((int) xpos.get(0), (int) ypos.get(0));
			System.out.println("Territory Selected: " + territorySelected);
		}
	}

	public static void main(String[] args) {
		new Game().run();
	}

}