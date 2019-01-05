package mainframe;
import rendering.Texture;

public class TestMap extends Map {

	public TestMap() {
		base_map = new Texture("testmap.png");
		territories.add(new Territory(1, -1, 255, 0, 0));
		territories.add(new Territory(2, -1, 125, 0, 0));
		territories.add(new Territory(3, -1, 0, 125, 125));
		territories.add(new Territory(4, -1, 0, 0, 125));
		territories.add(new Territory(5, -1, 0, 0, 255));
	}

}