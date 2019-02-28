package mainframe;
import java.awt.image.BufferedImage;

import rendering.Texture;

public class TestMap extends Map {

	public TestMap() {
		base_map = new Texture("testmap.png");
		truecolor = loadImage("./res/testmap_truecolor.png");
		
		territories.add(new Territory(0, 1, 255, 0, 0, 0, 2));
		territories.add(new Territory(1, 1, 0, 125, 125, 2, 5));
		territories.add(new Territory(2, 1, 125, 0, 0, 1, 1));
		territories.add(new Territory(3, 1, 0, 0, 125));
		territories.add(new Territory(4, 1, 0, 0, 255));
		
		territory_textures.add(new Texture("territory1.png"));
		territory_textures.add(new Texture("territory2.png"));
		territory_textures.add(new Texture("territory3.png"));
		territory_textures.add(new Texture("territory4.png"));
		territory_textures.add(new Texture("territory5.png"));
	}

}
