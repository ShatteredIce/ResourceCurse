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

	// The window handle
	private long window;
	//Rendering variables
	final double[] textureCoords = {0, 0, 0, 1, 1, 0, 1, 1};
	final int[] indices = {0, 1, 2, 2, 1, 3};
	final double[] placeholder = {0, 0, 0, 0, 0, 0, 0, 0};
	Model model;
	
	//Setup for variables for game frame
	int WINDOW_WIDTH = 840;
	int WINDOW_HEIGHT = 640;
	
	int gameScreenWidth = 640;
	int gameScreenHeight = 640;
	
	//Setup variables for camera
	public double viewX = 0;
	public double viewY = 0;
	public double cameraSpeed = 10;
	public double cameraWidth = 840;
	public double cameraHeight = 640;
	
	public int windowXOffset = 0;
	public int windowYOffset = 0;
	
	Map gamemap;

	public void run() {

		initGLFW();
		setup();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void initGLFW() {
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
		window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "ResourceCurse [Development]", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
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
			if ( button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
				System.out.println("Left Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
			}
			else if( button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
				System.out.println("Right Mouse Button: " + xpos.get(0) + " " + ypos.get(0));
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

	private void setup() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
		
		//Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_TEXTURE_2D);
		
		model = new Model(placeholder, textureCoords, indices);
		gamemap = new TestMap();

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {			
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			gameLoop();

			glfwSwapBuffers(window); // swap the color buffers
		}
	}
	
	public void gameLoop() {
		projectRelativeCameraCoordinates();
		gamemap.getTexture().bind();
		model.render(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public static void main(String[] args) {
		new Game().run();
	}
	
	//Screen projection based on relative camera coordinates
	public void projectRelativeCameraCoordinates(){
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // Resets any previous projection matrices
        glOrtho((-windowXOffset * getWidthScalar()) + viewX, viewX + cameraWidth + (windowXOffset * getWidthScalar()), viewY + cameraHeight + ((windowYOffset)* getHeightScalar()), viewY + ((-windowYOffset) * getHeightScalar()), 1, -1);
        glMatrixMode(GL_MODELVIEW);
	}
		
	//Screen projection based on true window coordinates
	public void projectTrueWindowCoordinates(){
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // Resets any previous projection matrices
        glOrtho(-windowXOffset, WINDOW_WIDTH + windowXOffset, WINDOW_HEIGHT + windowYOffset, -windowYOffset, 1, -1);
        glMatrixMode(GL_MODELVIEW);
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

}