package rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;


import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Model {
	
	private int draw_count;
	private int v_id;
	private int t_id;
	private int i_id;
	
	public Model(double[] vertices, double[] textureCoords, int[] indices){
		draw_count = indices.length;
		
		v_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_DYNAMIC_DRAW);
		
		t_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, t_id);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(textureCoords), GL_DYNAMIC_DRAW);
		
		i_id = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
		
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
		buffer.put(indices);
		buffer.flip();
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}
	
	public void destroy(){
		glDeleteBuffers(v_id);
		glDeleteBuffers(t_id);
		glDeleteBuffers(i_id);
	}
	
	public void setTextureCoords(double[] textureCoords){
		glBindBuffer(GL_ARRAY_BUFFER, t_id);
		glBufferSubData(GL_ARRAY_BUFFER, 0, textureCoords);
	}
	
	public void render(double[] vertices) {
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		
//		glEnableClientState(GL_VERTEX_ARRAY);
//		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glVertexAttribPointer(0, 3, GL_DOUBLE, false, 0, 0);
//		glVertexPointer(2, GL_DOUBLE, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, t_id);
//		glTexCoordPointer(2, GL_DOUBLE, 0, 0);
		glVertexAttribPointer(1, 2, GL_DOUBLE, false, 0, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
		glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
//		glDisableClientState(GL_VERTEX_ARRAY);
//		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);


	}
	
	public void render(double x1, double y1, double x2, double y2) {
		
		double[] vertices = {x1, y1, 0, x1, y2, 0, x2, y1, 0, x2, y2, 0};
		render(vertices);
	}

	private DoubleBuffer createBuffer(double[] data) {
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
