package mainframe;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import rendering.Model;
import rendering.Shader;

public class GameEngine {
	
	static Game game;
	static GameClient client;
	
	boolean isHost = true;
	boolean cameraMovement = false;
	
	// The window handle
	private long window;
	
	final int WINDOW_WIDTH = 1000;
	final int WINDOW_HEIGHT = 640;
	
	int worldWidth = 2000;
	int worldHeight = 1050;
	
	//Setup for variables for game frame
	
	int gameScreenWidth = 840;
	int gameScreenHeight = 640;
	
	//Setup variables for camera
	public double viewX = 0;
	public double viewY = 0;
	public double cameraSpeed = 10;
	public double cameraWidth = 840;
	public double cameraHeight = 640;
	public final int CAMERA_SPEED = 30;
	
	public int windowXOffset = 0;
	public int windowYOffset = 0;
	
	//Rendering variables
	final double[] textureCoords = {0, 0, 1, 0, 1, 1, 0, 1};
	final int[] indices = {0, 1, 2, 2, 3, 0};
	final double[] placeholder = {-0.6, 0.6, 0, 0, 0.5, 0, 0.5, -0.5, 0};
	
	Model model;
	
	boolean panLeft = false;
    boolean panRight = false;
    boolean panUp = false;
    boolean panDown = false;
    
    String name;

	public GameEngine(Game newgame, String newname) {
		game = newgame;
		name = newname;
	}
	
	public GameEngine(GameClient newclient, String newname) {
		client = newclient;
		isHost = false;
		name = newname;
	}
	
	public void create() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		// Create the window
		window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, name, NULL, NULL);
		if ( window == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			}
			//pan camera
			else if ( key == GLFW_KEY_LEFT && action == GLFW_PRESS )
				panLeft = true;
			else if ( key == GLFW_KEY_LEFT && action == GLFW_RELEASE )
				panLeft = false;
			
			else if ( key == GLFW_KEY_RIGHT && action == GLFW_PRESS )
				panRight = true;
			else if ( key == GLFW_KEY_RIGHT && action == GLFW_RELEASE )
				panRight = false;
			
			else if ( key == GLFW_KEY_UP && action == GLFW_PRESS )
				panUp = true;
			else if ( key == GLFW_KEY_UP && action == GLFW_RELEASE )
				panUp = false;
			
			else if ( key == GLFW_KEY_DOWN && action == GLFW_PRESS )
				panDown = true;
			else if ( key == GLFW_KEY_DOWN && action == GLFW_RELEASE )
				panDown = false;
			else{
				if(isHost) {
					game.onKeyPressed(window, key, scancode, action, mods);
				}
				else {
					client.onKeyPressed(window, key, scancode, action, mods);
				}
			}
		});
		
		//mouse clicks
		glfwSetMouseButtonCallback (window, (window, button, action, mods) -> {
			DoubleBuffer xpos = BufferUtils.createDoubleBuffer(3);
			DoubleBuffer ypos = BufferUtils.createDoubleBuffer(3);
			glfwGetCursorPos(window, xpos, ypos);
			//convert the glfw coordinate to our coordinate system
			xpos.put(0, Math.min(Math.max(xpos.get(0), windowXOffset), WINDOW_WIDTH + windowXOffset));
			ypos.put(0, Math.min(Math.max(ypos.get(0), windowYOffset), WINDOW_HEIGHT + windowYOffset));
			//relative camera coordinates
			xpos.put(1, getWidthScalar() * (xpos.get(0) - windowXOffset) + viewX);
			ypos.put(1, getHeightScalar() * (ypos.get(0) - windowYOffset) + viewY);
			//true window coordinates
			xpos.put(2, xpos.get(0) - windowXOffset);
			ypos.put(2, ypos.get(0) - windowYOffset);
			if(isHost) {
				game.onMouseClick(button, action, xpos, ypos);
			}
			else {
				client.onMouseClick(button, action, xpos, ypos);
			}
		});
		//mouse scroll
		glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			//If they scrolled up, zoom in. If they scrolled down, zoom out.
			if (yoffset > 0) {
				updateZoomLevel(false);
			}
			if (yoffset < 0) {
				updateZoomLevel(true);
			}
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}
	
	public void setup() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		//Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_TEXTURE_2D);
		
		model = new Model(placeholder, textureCoords, indices);
		
		
	}
	
	public void destroy() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public long getWindowHandle() {
		return window;
	}
		
	//Scalars to help calculation
	public double getWidthScalar(){
		return(double) cameraWidth / (double) WINDOW_WIDTH;
	}
		
	public double getHeightScalar(){
		return(double) cameraHeight / (double) WINDOW_HEIGHT;
	}
	
	public double mapWidthScalar() {
		return (double) gameScreenWidth / (double) WINDOW_WIDTH;
	}
	
	public double mapHeightScalar() {
		return (double) gameScreenHeight / (double) WINDOW_HEIGHT;
	}
	
	public int getWindowWidth() {
		return WINDOW_WIDTH;
	}
	public int getWindowHeight() {
		return WINDOW_HEIGHT;
	}
	
	public double getGLCoordinateX(double x) {
		if(cameraMovement) {
			return ((x - viewX) / (cameraWidth / 2)) - 1;
		}
		else {
			return ((x) / (WINDOW_WIDTH / 2)) - 1;
		}
		
	}
	
	public double getGLCoordinateY(double y) {
		if(cameraMovement) {
			return -1*(((y - viewY) / (cameraHeight / 2)) - 1);
		}
		else {
			return -1*(((y) / (WINDOW_HEIGHT / 2)) - 1);
		}
	}
	
	public void render(double x1, double y1, double x2, double y2) {
		model.render(getGLCoordinateX(x1), getGLCoordinateY(y1), getGLCoordinateX(x2), getGLCoordinateY(y2));
	}
	
	public void render(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		model.render(new double[]{getGLCoordinateX(x1) ,getGLCoordinateY(y1) , 0, getGLCoordinateX(x2), getGLCoordinateY(y2), 0,
				getGLCoordinateX(x3), getGLCoordinateY(y3), 0, getGLCoordinateX(x4), getGLCoordinateY(y4), 0});
	}

	//scroll view camera
	public void moveCamera() {
		if (panLeft) {
			viewX = Math.max(0, viewX - cameraWidth / CAMERA_SPEED);
		}
		if (panRight) {
			viewX = Math.min(worldWidth - cameraWidth * (double) gameScreenWidth / (double) WINDOW_WIDTH, viewX + cameraWidth / CAMERA_SPEED);
		}
		if (panDown) {
			viewY = Math.min(worldHeight - cameraHeight * (double) gameScreenHeight / (double) WINDOW_HEIGHT, viewY + cameraHeight / CAMERA_SPEED);
		}
		if (panUp) {
			viewY = Math.max(0, viewY - cameraHeight / CAMERA_SPEED);
		}
	}
	
	//Zoom camera in or out
	public void updateZoomLevel(boolean zoomOut){
		DoubleBuffer xpos = BufferUtils.createDoubleBuffer(3);
		DoubleBuffer ypos = BufferUtils.createDoubleBuffer(3);
		glfwGetCursorPos(window, xpos, ypos);
		//Convert the glfw coordinate to our coordinate system
		xpos.put(0, Math.min(Math.max(xpos.get(0), windowXOffset), WINDOW_WIDTH + windowXOffset));
		ypos.put(0, Math.min(Math.max(ypos.get(0), windowYOffset), WINDOW_HEIGHT + windowYOffset));
		//Relative camera coordinates
		xpos.put(1, getWidthScalar() * (xpos.get(0) - windowXOffset) + viewX);
		ypos.put(1, getHeightScalar() * (ypos.get(0) - windowYOffset) + viewY);
		//True window coordinates
		xpos.put(2, xpos.get(0) - windowXOffset);
		ypos.put(2, ypos.get(0) - windowYOffset);
		
		boolean mouseInFrame = false;
		double oldX = xpos.get(1);
		double oldY = ypos.get(1);
		double xAxisDistance = 0;
		double yAxisDistance = 0;
		
		if(xpos.get(2) > 0 && xpos.get(2) < gameScreenWidth && ypos.get(2) > 0 && ypos.get(2) < gameScreenHeight){
			mouseInFrame = true;
			xAxisDistance = xpos.get(2)/WINDOW_WIDTH;
			yAxisDistance = ypos.get(2)/WINDOW_HEIGHT;
			//System.out.println(xAxisDistance + " " + yAxisDistance);
		}
		
		int MIN_WIDTH = 100;
		int MIN_HEIGHT = 100;
		int MAX_WIDTH = WINDOW_WIDTH;
		int MAX_HEIGHT = WINDOW_HEIGHT;
		
		double zoomLevel = 4d/3d;
		
		if(!mouseInFrame) {
			oldX = viewX + (cameraWidth * gameScreenWidth/WINDOW_WIDTH)/2;
			oldY = viewY + (cameraHeight * gameScreenHeight/WINDOW_HEIGHT)/2;
			xAxisDistance = (gameScreenWidth/2d/WINDOW_WIDTH);
			yAxisDistance = (gameScreenHeight/2d/WINDOW_HEIGHT);
		}
		
		//Zooms out camera
		if(zoomOut){
			if(cameraWidth * zoomLevel <= MAX_WIDTH && cameraHeight * zoomLevel <= MAX_HEIGHT){
				cameraWidth *= zoomLevel;
				cameraHeight *= zoomLevel;
				viewX = oldX - cameraWidth * xAxisDistance;
				viewY = oldY - cameraHeight * yAxisDistance;
//					System.out.println(viewX + " " + cameraWidth); 
				double gameScreenCameraWidth = cameraWidth * gameScreenWidth / WINDOW_WIDTH;
				double gameScreenCameraHeight = cameraHeight * gameScreenHeight / WINDOW_HEIGHT;
				if(viewX + gameScreenCameraWidth > worldWidth){
					viewX = worldWidth - gameScreenCameraWidth;
				}
				if(viewY + gameScreenCameraHeight > worldHeight){
					viewY = worldHeight - gameScreenCameraHeight;
				}
				if(viewX < 0){
					viewX = 0;
				}
				if(viewY < 0){
					viewY = 0;
				}
			}
		}
		else{ // Zooms in camera
			if(cameraWidth / zoomLevel >= MIN_WIDTH && cameraHeight / zoomLevel >= MIN_HEIGHT){
				cameraWidth /= zoomLevel;
				cameraHeight /= zoomLevel;
				viewX = oldX - cameraWidth * xAxisDistance;
				viewY = oldY - cameraHeight * yAxisDistance;
				//System.out.println(viewX + " " + viewY);
			}
		}
	}
	
	public void toggleCamera(boolean b) {
		cameraMovement = b;
	}
}
