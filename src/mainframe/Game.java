package mainframe;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;

import java.nio.DoubleBuffer;

import rendering.Model;
import rendering.Shader;
import rendering.Texture;

public class Game {

	//Rendering variables
	final double[] textureCoords = {0, 0, 0, 1, 1, 0, 1, 1};
	final int[] indices = {0, 1, 2, 2, 1, 3};
	final double[] placeholder = {0, 0, 0, 0, 0, 0, 0, 0};
	Model model;
	Shader shader;
	
	GameEngine lwjgl3 = new GameEngine(this);
	
	Map gamemap;

	public void run() {
		lwjgl3.create();
		start();
		lwjgl3.destroy();
	}

	private void start() {
		
		lwjgl3.setup();
		
		model = new Model(placeholder, textureCoords, indices);
		shader = new Shader("shader");
		gamemap = new TestMap();

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
		
//		Texture tex = new Texture("testmap.png");
		
		lwjgl3.projectRelativeCameraCoordinates();
		
		shader.bind();
//		shader.setUniform("sampler", 0);
//		tex.bind(0);
		model.render(-0.5, -0.5, 0.5, 0.5);
		
//		gamemap.getTerritoryTextures().get(0).bind(0);
//		model.render(0, 0, lwjgl3.getWindowWidth(), lwjgl3.getWindowHeight());
//		
//		gamemap.getTerritoryTextures().get(1).bind(0);
//		model.render(0, 0, lwjgl3.getWindowWidth(), lwjgl3.getWindowHeight());
//				
//		gamemap.getTerritoryTextures().get(2).bind(0);
//		model.render(0, 0, lwjgl3.getWindowWidth(), lwjgl3.getWindowHeight());



	}
	
	public void onMouseClick(int button, int action, DoubleBuffer xpos, DoubleBuffer ypos) {
		if ( button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			System.out.println("Left Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
			int t_id = gamemap.getTerritoryClicked((int) xpos.get(0), (int) ypos.get(0));
			if(t_id != -1) {
				System.out.println("Territory Clicked: " + t_id);
			}
		}
		else if( button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
			System.out.println("Right Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
		}
	}

	public static void main(String[] args) {
		new Game().run();
	}

}