package mainframe;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


import java.nio.DoubleBuffer;
import java.util.ArrayList;

import rendering.Model;
import rendering.Shader;
import rendering.Texture;

public class Game {
	
	GameEngine engine = new GameEngine(this);
	
	Shader shader;
	Map gamemap;
	Texture maptex;
	Texture unittex;
	Texture selectedunittex;
	Texture movearrowtex;

	Unit selectedUnit = null;
	
	boolean nextTurn = true;
	int turnNum = 1;
	int deployType = 0;

	boolean shiftPressed = false;
	
	ArrayList<Player> players = new ArrayList<Player>();
	Player neutral = new Player(0, 0.8f, 0.8f, 0.8f);
	Player controlledPlayer = new Player(1, 0.5f, 0, 0);


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
		unittex = new Texture("unit.png");
		selectedunittex = new Texture("selectedunit.png");
		movearrowtex = new Texture("movearrow.png");

		
		//players id start at 1
		players.add(neutral);
		players.add(controlledPlayer);
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
			float[] color = players.get(gamemap.getTerritories().get(i).getOwner()).getColor();
			shader.setUniform("red", color[0]);
			shader.setUniform("green", color[1]);
			shader.setUniform("blue", color[2]);
			engine.render(0, 0, 840, 640);
		}
		
		//draw all units
		unittex.bind(0);
		shader.setUniform("red", 0f);
		shader.setUniform("green", 0f);
		shader.setUniform("blue", 0f);
		for (Player p : players) {
			for (Unit u : p.getUnits()) {
				int center[] = gamemap.getTerritories().get(u.getLocation()).center;
				if(u.getTarget() != -1) {
					movearrowtex.bind(0);
					int targetcenter[] = gamemap.getTerritories().get(u.getTarget()).center;
					double invslope = -1/(((double) targetcenter[1] - center[1]) / ((double) targetcenter[0] - center[0]));
					double magnitude = Math.sqrt(Math.pow(invslope, 2) + 1);
					double rise = invslope / magnitude;
					double run = 1 / magnitude;
					engine.render(center[0] + 40*run, center[1] + 40*rise, center[0] - 40*run, center[1] - 40*rise,
							targetcenter[0] - 40*run, targetcenter[1] - 40*rise, targetcenter[0] + 40*run, targetcenter[1] + 40*rise);
					unittex.bind(0);
				}
				engine.render(center[0] - 40, center[1] - 40, center[0] + 40, center[1] + 40);
			}
		}
		if(selectedUnit != null) {
			float[] color = controlledPlayer.getColor();
			shader.setUniform("red", color[0]);
			shader.setUniform("green", color[1]);
			shader.setUniform("blue", color[2]);
			selectedunittex.bind(0);
			int center[] = gamemap.getTerritories().get(selectedUnit.getLocation()).center;
			engine.render(center[0] - 40, center[1] - 40, center[0] + 40, center[1] + 40);
		}
		
		//give players resources
		if(nextTurn == true) {
			moveUnits();
			System.out.println("------------------[Turn " + turnNum + "]--------------------");
			for (int i = 1; i < players.size(); i++) {
				Player p = players.get(i);
				p.subResource(1, p.getResources()[1]); //clear all excess mil power
			}
			for (Territory t : gamemap.getTerritories()) {
				players.get(t.getOwner()).addResource(t.getResourceType(), t.getResourceAmt());
				players.get(t.influenced_by).addResource(t.getResourceType(),t.getResourceAmt());
			}
			for (int i = 1; i < players.size(); i++) {
				Player p = players.get(i);
				p.subResource(1, p.numUnits());
				System.out.println("Player " + p.getId() + ": " + p.checkResource(0) + " Diplomatic  " + p.checkResource(1) + " Military  " + p.checkResource(2) + " Economic");
			}
			System.out.println("----------------------------------------------");
			nextTurn = false;
			turnNum++;
		}

		tick = (tick + 1) % tick_cycle;
		
		engine.moveCamera();

	}
	
	public void moveUnits() {
		for (Player p : players) {
			for (Unit u : p.getUnits()) {
				if(u.getTarget() != -1) {
					u.setLocation(u.getTarget());
					u.setTarget(-1);
				}
			}
		}
	}

	public boolean checkAdjacent(int location, int target){
	    for(int adj : gamemap.getTerritories().get(target).adjacent){
	        if(location == adj){
	            return true;
            }
        }
        return false;
    }
	
	//mouse clicks
	public void onMouseClick(int button, int action, DoubleBuffer xpos, DoubleBuffer ypos) {
		if ( button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			if(selectedUnit != null) {
				selectedUnit = null;
			}
			System.out.println("Left Mouse Button: " + xpos.get(1) + " " + ypos.get(1));
			int t_id = gamemap.getTerritoryClicked((int) xpos.get(1), (int) ypos.get(1));
			if(t_id != -1) {
				//deploy diplomatic control points
				if(deployType == 0 && controlledPlayer.getResources()[0] > 0) {
					gamemap.getTerritories().get(t_id).addDiplomaticPoints(1, controlledPlayer.getId());
					controlledPlayer.subResource(0, 1);
				}
				//deploy mil units on controlled territories only
				else if(deployType == 1 && controlledPlayer.getResources()[1] > 0 && gamemap.getTerritories().get(t_id).getOwner() == controlledPlayer.getId()) {
					controlledPlayer.addUnit(new Unit(t_id));
					controlledPlayer.subResource(1, 1);
				}
				else if(deployType == 2 && controlledPlayer.getResources()[2] > 0 && gamemap.getTerritories().get(t_id).getOwner() == controlledPlayer.getId()){
				    gamemap.getTerritories().get(t_id).addEconomicPoints(1);
				    controlledPlayer.subResource(2,1);
                }
			}
	
		}
		else if( button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
			System.out.println("Right Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
			int t_id = gamemap.getTerritoryClicked((int) xpos.get(1), (int) ypos.get(1));
			if(t_id != -1) {
				if(selectedUnit == null) {
					for (int i = 0; i < controlledPlayer.getUnits().size(); i++) {
						System.out.println(controlledPlayer.getUnits().get(i).getLocation() +  " " + t_id);
						if(controlledPlayer.getUnits().get(i).getLocation() == t_id) {
							selectedUnit = controlledPlayer.getUnits().get(i);
						}
					}
				}
				else if(selectedUnit != null) {
					if(t_id != selectedUnit.getLocation() && checkAdjacent(selectedUnit.getLocation(),t_id)) {
						selectedUnit.setTarget(t_id);
					}
					selectedUnit = null;
				}
			}
		}
		else if( button == GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW_PRESS) {
			if(selectedUnit != null) {
				selectedUnit = null;
			}
			int t_id = gamemap.getTerritoryClicked((int) xpos.get(0), (int) ypos.get(0));
			if(t_id != -1) {
				if(shiftPressed) {
					System.out.println("Territory Clicked: " + t_id);
					gamemap.getTerritories().get(t_id).setOwner((gamemap.getTerritories().get(t_id).getOwner() + 1) % players.size());
				}
				else {
					System.out.println("Territory Clicked: " + t_id);
					System.out.println("Owner: " + gamemap.getTerritories().get(t_id).owner_id);
					System.out.println("Influenced By: " + gamemap.getTerritories().get(t_id).influenced_by);
					int[] diplo = gamemap.getTerritories().get(t_id).getDiplomaticPoints();
					for (int p = 1; p < players.size(); p++) {
						System.out.println("Player " + (p) + " : "+ diplo[p] + " DP");
					}
				}
			}
		}
	}

	//key presses
	public void onKeyPressed(long window, int key, int scancode, int action, int mods) {
		if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
			glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		if ( key == GLFW_KEY_ENTER && action == GLFW_RELEASE )
			nextTurn = true;
		if ( key == GLFW_KEY_0 && action == GLFW_RELEASE ) {
			deployType = 0;
			System.out.println("Deploying Diplomatic Points");
		}
		if ( key == GLFW_KEY_1 && action == GLFW_RELEASE ) {
			deployType = 1;
			System.out.println("Deploying Military Units");
		}
        if ( key == GLFW_KEY_2&& action == GLFW_RELEASE ) {
            deployType = 2;
            System.out.println("Deploying Economic Points");
        }
		if ( key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS )
			shiftPressed = true;
		if ( key == GLFW_KEY_LEFT_SHIFT && action == GLFW_RELEASE )
			shiftPressed = false;
		
	}
	
	public static void main(String[] args) {
		new Game().run();
	}

}