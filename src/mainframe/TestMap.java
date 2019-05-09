package mainframe;

import rendering.Texture;

public class TestMap extends Map {

	public TestMap() {
		base_map = new Texture("testmap.png");

		truecolor = loadImage("./res/testmap_truecolor.png");

		territories.add(new Territory(0, 0, 255, 0, 0, 260, 180, 0, 1, new int[]{1,2,3}));
		territories.add(new Territory(1, 0, 0, 125, 125, 320, 435, 2, 1, new int[]{0,2,4}));
		territories.add(new Territory(2, 0, 125, 0, 0, 390, 255, 1, 2,new int[]{0,1,3,4}));
		territories.add(new Territory(3, 0, 0, 0, 125, 580, 215, 1, 2, new int[]{0,2,4}));
		territories.add(new Territory(4, 0, 0, 0, 255, 570, 405, 1, 2,new int[]{1,2,3}));
		
		territory_textures.add(new Texture("territory1.png"));
		territory_textures.add(new Texture("territory2.png"));
		territory_textures.add(new Texture("territory3.png"));
		territory_textures.add(new Texture("territory4.png"));
		territory_textures.add(new Texture("territory5.png"));
	}

}
