package mainframe;
import java.util.ArrayList;

import rendering.Texture;

public abstract class Map {
	
	Texture base_map;
	
	ArrayList<Territory> territories = new ArrayList<Territory>();
	
	public Map() {
		
	}
	
	public int findTerritory(int red, int green, int blue) {
		for (int i = 0; i < territories.size(); i++) {
			if(territories.get(i).checkTrueColor(red, green, blue)) {
				return territories.get(i).getId();
			}
		}
		return -1;
	}
	
	public Texture getTexture() {
		return base_map;
	}

}
