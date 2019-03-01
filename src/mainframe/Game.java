package mainframe;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import rendering.Model;
import rendering.Shader;
import rendering.Texture;

public class Game {
	
	GameEngine engine = new GameEngine(this);
	
	Shader shader;
	Map gamemap;

	int territorySelected;

	
	ArrayList<Player> players = new ArrayList<Player>();

	int tick = 1;
	int tick_cycle = 100;
	
	public void run() {
		engine.create();
		start();
		engine.destroy();
	}

	private void start() {
		
		engine.setup();
		
		shader = new Shader("shader");
		gamemap = new TestMap();

		territorySelected = -1;


		
		//players id start at 1
		players.add(new Player(1, 0.5f, 0, 0));
		players.add(new Player(2, 0, 0.5f, 0.5f));
		players.add(new Player(3, 0, 0, 0.5f));
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(engine.getWindowHandle()) ) {			
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			gameLoop();

			glfwSwapBuffers(engine.getWindowHandle()); // swap the color buffers
		}
		
	}
	
	public void gameLoop() {
			
		shader.bind();
		shader.setUniform("sampler", 0);
		
		shader.setUniform("red", 0f);
		shader.setUniform("green", 0f);
		shader.setUniform("blue", 0f);

		gamemap.bindTexture();
		engine.render(0, 0, 840, 640);
		
		//draw all territories
		for (int i = 0; i < gamemap.getTerritories().size(); i++) {
			gamemap.getTerritoryTextures().get(i).bind(0);
			float[] color = players.get(gamemap.getTerritories().get(i).getOwner() - 1).getColor();
			shader.setUniform("red", color[0]);
			shader.setUniform("green", color[1]);
			shader.setUniform("blue", color[2]);
			engine.render(0, 0, 840, 640);
		}
		
		//give players resources
		if(tick == 0) {
			for (Territory t : gamemap.getTerritories()) {
				players.get(t.getOwner()-1).addResource(t.getResourceType(), t.getResourceAmt());
			}
			for (Player p : players) {
				System.out.println("Player " + p.getId() + ": " + p.checkResource(0) + " Oil  " + p.checkResource(1) + " Steel  " + p.checkResource(2) + " Food");
			}
		}

		tick = (tick + 1) % tick_cycle;
		
		engine.moveCamera();

	}
	
	public void onMouseClick(int button, int action, DoubleBuffer xpos, DoubleBuffer ypos) {
		if ( button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			System.out.println("Left Mouse Button: " + xpos.get(1) + " " + ypos.get(1));
			int t_id = gamemap.getTerritoryClicked((int) xpos.get(1), (int) ypos.get(1));
			if(t_id != -1) {
				if(territorySelected != -1){
					if(territorySelected != t_id) {
						gamemap.territories.get(t_id).incrementNumUnits(gamemap.territories.get(territorySelected).getNumUnits());
						gamemap.territories.get(territorySelected).setNumUnits(0);
						for(Unit unit: players.get(0).units){
							if(unit.getLocation() == territorySelected){
								unit.setLocation(t_id);
							}
						}
						System.out.println("Territory Clicked: " + t_id);
						System.out.println("Units: " + gamemap.territories.get(t_id).getNumUnits());
					}
					territorySelected = -1;
				}else {
					players.get(0).units.add(new Unit(t_id));
					gamemap.territories.get(t_id).incrementNumUnits(1);
					System.out.println("Territory Clicked: " + t_id);
					System.out.println("Units: " + gamemap.territories.get(t_id).getNumUnits());
				}
				
			}
		}
		else if( button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
			System.out.println("Right Mouse Button: " + xpos.get(1) + " " + ypos.get(1));
			territorySelected = gamemap.getTerritoryClicked((int) xpos.get(1), (int) ypos.get(1));
			System.out.println("Territory Selected: " + territorySelected);
			if(territorySelected != -1) {
				System.out.println("Units: " + gamemap.territories.get(territorySelected).getNumUnits());
			}

		}
		else if( button == GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW_PRESS) {
			int t_id = gamemap.getTerritoryClicked((int) xpos.get(1), (int) ypos.get(1));
			if(t_id != -1) {
				System.out.println("Territory Clicked: " + t_id);
				gamemap.getTerritories().get(t_id).setOwner(((gamemap.getTerritories().get(t_id).getOwner()) % players.size()) + 1);
			}
		}
	}

	public static void main(String[] args) {
		new Game().run();
	}

}