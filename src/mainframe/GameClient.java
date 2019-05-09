package mainframe;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Scanner;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import com.esotericsoftware.kryonet.*;

import packets.MapInfo;
import packets.PlayerInfo;
import packets.TerritoryInfo;
import packets.TurnStatus;
import packets.UnitInfo;
import packets.UnitPositions;
import rendering.GameTextures;
import rendering.Shader;
import rendering.Texture;

public class GameClient extends Listener {
	
	GameEngine engine = new GameEngine(this, "ResourceCurse [Development] - Client");
	
	Shader shader;
	Map gamemap;
	Texture maptex;
	
	static GameTextures gametextures;

	//networking
	static Client client;
	//ip of server
	static String ip = "localhost";
	static int tcpPort = 27960;
	static int udpPort = 27960;
	
	float[][] colors = {
			{0.8f, 0.8f, 0.8f},
			{0.5f, 0, 0},
			{0, 0.5f, 0},
			{0, 0, 0.5f}
	};
	
	int myPlayerId = -1;
	
	Unit selectedUnit = null;
	
	int gameState = 0; //0 = waiting for players, 1 = started, 2 = won, 3 = lost
	int turnNum = 1;
	
	boolean nextTurn = false;
		
	int deployType = 0;

	boolean shiftPressed = false;
	
	ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();
	ArrayList<UnitInfo> units = new ArrayList<UnitInfo>();
	
	
	public void run() throws IOException{
		
		//create client
		client = new Client(100000, 100000);
		//register packets
		client.getKryo().register(java.util.ArrayList.class);
		client.getKryo().register(double[].class);
		client.getKryo().register(float[].class);
		client.getKryo().register(int[].class);
		client.getKryo().register(int[][].class);
		client.getKryo().register(UnitInfo.class);
		client.getKryo().register(UnitPositions.class);
		client.getKryo().register(TurnStatus.class);
		client.getKryo().register(PlayerInfo.class);
		client.getKryo().register(MapInfo.class);
		client.getKryo().register(TerritoryInfo.class);
		//start the client
		client.start();
		
		Scanner scanner = new Scanner(System.in);
		
		if(ip.equals("manual")) {
			System.out.print("Enter Host IP: ");
			ip = scanner.nextLine();
		}
		while(true) {
			try {
				//connect to the server
				client.connect(5000, ip, tcpPort, udpPort);
				break;
			}
			catch(IOException e) {
				System.out.println("Failed to connect to " + ip);
				System.out.print("Enter Host IP: ");
				ip = scanner.nextLine();
			}
			if(ip.equals("")) {
				System.exit(0);
			}
		}
		
		scanner.close();
		
		client.addListener(this);
		myPlayerId = client.getID() + 1;
		
		System.out.println("Client Initialized");
		
		engine.create();
		start();
		engine.destroy();
		
		client.stop();
		
	}

	private void start() {
		
		engine.setup();
		
		shader = new Shader("shader");
		gamemap = new TestMap();
		
		gametextures = new GameTextures();
						
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
	
	//networking functions
	@Override
	public void connected(Connection c){
	
	}
	
	
	@Override
	public void received(Connection c, Object obj) {
		if(obj instanceof TurnStatus){
			TurnStatus packet = (TurnStatus) obj;
			if(gameState == 0 && packet.getStatus()) {
				gameState = 1;
			}
			nextTurn = true;
		}
		else if(obj instanceof UnitPositions){
			UnitPositions packet = (UnitPositions) obj;
			units = packet.getUnitdata();
		}
		else if(obj instanceof PlayerInfo){
			PlayerInfo packet = (PlayerInfo) obj;
			players.add(packet);
		}
		else if(obj instanceof MapInfo){
			ArrayList<TerritoryInfo> territoryData = ((MapInfo) obj).getTerritories();
			for (int i = 0; i < territoryData.size(); i++) {
				gamemap.getTerritories().get(territoryData.get(i).getId()).setOwner(territoryData.get(i).getOwnerId());
			}
		}
	}
	
	@Override
	public void disconnected(Connection c){
		
	}
	
	public void gameLoop() {
			
		shader.bind();
		shader.setUniform("sampler", 0);
		
		shader.setUniform("red", 0f);
		shader.setUniform("green", 0f);
		shader.setUniform("blue", 0f);
		
		if(gameState == 1) {
			gamemap.bindTexture();
			engine.render(0, 0, 840, 640);
			
			//draw territories
			for (int i = 0; i < gamemap.getTerritories().size(); i++) {
				gamemap.getTerritoryTextures().get(i).bind(0);
				float[] color = players.get(gamemap.getTerritories().get(i).getOwner()).getColor();
				shader.setUniform("red", color[0]);
				shader.setUniform("green", color[1]);
				shader.setUniform("blue", color[2]);
				engine.render(0, 0, 840, 640);
			}
			
			//draw units
			gametextures.loadTexture(0);
			for (UnitInfo u : units) {
				float[] color = players.get(u.getOwnerId()).getColor();
				int center[] = gamemap.getTerritories().get(u.getLocation()).center;
				if(u.getTarget() != -1) {
					if(u.isSupporting()) {
						gametextures.loadTexture(3);
					}
					else {
						gametextures.loadTexture(2);
					}
					int targetcenter[] = gamemap.getTerritories().get(u.getTarget()).center;
					double invslope = -1/(((double) targetcenter[1] - center[1]) / ((double) targetcenter[0] - center[0]));
					double magnitude = Math.sqrt(Math.pow(invslope, 2) + 1);
					double rise = invslope / magnitude;
					double run = 1 / magnitude;
					shader.setUniform("red", 0f);
					shader.setUniform("green", 0f);
					shader.setUniform("blue", 0f);
					engine.render(center[0] + 40*run, center[1] + 40*rise, center[0] - 40*run, center[1] - 40*rise,
							targetcenter[0] - 40*run, targetcenter[1] - 40*rise, targetcenter[0] + 40*run, targetcenter[1] + 40*rise);
					gametextures.loadTexture(0);
				}
				shader.setUniform("red", color[0]);
				shader.setUniform("green", color[1]);
				shader.setUniform("blue", color[2]);
				engine.render(center[0] - 40, center[1] - 40, center[0] + 40, center[1] + 40);
			}
			if(selectedUnit != null) {
	//			float[] color = controlledPlayer.getColor();
	//			shader.setUniform("red", color[0]);
	//			shader.setUniform("green", color[1]);
	//			shader.setUniform("blue", color[2]);
				gametextures.loadTexture(1);
				int center[] = gamemap.getTerritories().get(selectedUnit.getLocation()).center;
				engine.render(center[0] - 40, center[1] - 40, center[0] + 40, center[1] + 40);
			}
			
			//give players resources
			if(nextTurn) {
				System.out.println("------------------[Turn " + turnNum + "]--------------------");
				System.out.println("----------------------------------------------");
				turnNum++;
				nextTurn = false;
			}
				
			engine.moveCamera();
		}
		//display win message
		else if(gameState == 2) {
			
		}
		//display defeat message
		else if(gameState == 3) {
			
		}
	}
	
	//mouse clicks
	public void onMouseClick(int button, int action, DoubleBuffer xpos, DoubleBuffer ypos) {
//		if(gameState == 1) {
//			if ( button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
//				if(selectedUnit != null) {
//					selectedUnit = null;
//				}
//				System.out.println("Left Mouse Button: " + xpos.get(1) + " " + ypos.get(1));
//				int t_id = gamemap.getTerritoryClicked((int) xpos.get(1), (int) ypos.get(1));
//				if(t_id != -1) {
//					//deploy diplomatic control points
//					if(deployType == 0 && controlledPlayer.getResources()[0] > 0) {
//						gamemap.getTerritories().get(t_id).addDiplomaticPoints(1, controlledPlayer.getId());
//						controlledPlayer.subResource(0, 1);
//					}
//					//deploy mil units on controlled territories only
//					else if(deployType == 1 && controlledPlayer.getResources()[1] > 0 && gamemap.getTerritories().get(t_id).getOwner() == controlledPlayer.getId()) {
//						//can only have 1 unit per territory
//						boolean hasUnit = false;
//						for (Unit u : controlledPlayer.getUnits()) {
//							if(u.getLocation() == t_id) {
//								hasUnit = true;
//								break;
//							}
//						}
//						if(!hasUnit) {
//							Unit u = new Unit(t_id, controlledPlayer.getId());
//							gamemap.getTerritories().get(t_id).setOccupyingUnit(u);
//							controlledPlayer.addUnit(u);
//							controlledPlayer.subResource(1, 1);
//						}
//						//debug
//						else {
//							System.out.println("Cannot deploy unit");
//						}
//					}
//					else if(deployType == 2 && controlledPlayer.getResources()[2] > 0 && gamemap.getTerritories().get(t_id).getOwner() == controlledPlayer.getId()){
//					    gamemap.getTerritories().get(t_id).addEconomicPoints(1);
//					    controlledPlayer.subResource(2,1);
//	                }
//				}
//			}
//			else if( button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
//				System.out.println("Right Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
//				int t_id = gamemap.getTerritoryClicked((int) xpos.get(1), (int) ypos.get(1));
//				if(t_id != -1) {
//					if(selectedUnit == null) {
//						for (int i = 0; i < controlledPlayer.getUnits().size(); i++) {
//	//						System.out.println(controlledPlayer.getUnits().get(i).getLocation() +  " " + t_id);
//							if(controlledPlayer.getUnits().get(i).getLocation() == t_id) {
//								selectedUnit = controlledPlayer.getUnits().get(i);
//							}
//						}
//					}
//					//players move units
//					else if(selectedUnit != null) {
//						if(t_id != selectedUnit.getLocation() && checkAdjacent(selectedUnit.getLocation(),t_id)) {
//							boolean supporting = shiftPressed ? true : false;
//							for (Unit u : controlledPlayer.getUnits()) {
//								if(u != selectedUnit && u.getTarget() == t_id && u.isSupporting() == false) {
//									supporting = true;
//								}
//							}
//							selectedUnit.setTarget(t_id);
//							if(supporting) {
//								selectedUnit.setSupportMove(true);
//							}
//							else {
//								selectedUnit.setSupportMove(false);
//							}
//						}
//						else if(t_id == selectedUnit.getLocation()) {
//							selectedUnit.setTarget(-1);
//							selectedUnit.setSupportMove(false);
//						}
//						selectedUnit = null;
//					}
//				}
//				else {
//					selectedUnit = null;
//				}
//			}
//			else if( button == GLFW_MOUSE_BUTTON_MIDDLE && action == GLFW_PRESS) {
//				if(selectedUnit != null) {
//					selectedUnit = null;
//				}
//				int t_id = gamemap.getTerritoryClicked((int) xpos.get(0), (int) ypos.get(0));
//				if(t_id != -1) {
//					//change territory ownership
//					if(shiftPressed) {
//						System.out.println("Territory Clicked: " + t_id);
//						gamemap.getTerritories().get(t_id).setOwner((gamemap.getTerritories().get(t_id).getOwner() + 1) % players.size());
//					}
//					//get territory info
//					else {
//						System.out.println("Territory Clicked: " + t_id);
//						System.out.println("Owner: " + gamemap.getTerritories().get(t_id).owner_id);
//						System.out.println("Influenced By: " + gamemap.getTerritories().get(t_id).influenced_by);
//						int[] diplo = gamemap.getTerritories().get(t_id).getDiplomaticPoints();
//						for (int p = 1; p < players.size(); p++) {
//							System.out.println("Player " + (p) + " : "+ diplo[p] + " DP");
//						}
//					}
//				}
//			}
//		}
	}

	//key presses
	public void onKeyPressed(long window, int key, int scancode, int action, int mods) {
			if(key == GLFW_KEY_ENTER && action == GLFW_RELEASE) {
				client.sendTCP(new TurnStatus(true));
			}
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			if ( key == GLFW_KEY_ENTER && action == GLFW_RELEASE )
//				nextTurnArray[controlledPlayer.getId()] = true;
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
	
	public static void main(String[] args) throws IOException {
		new GameClient().run();
	}

}