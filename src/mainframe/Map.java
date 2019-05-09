package mainframe;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import rendering.Texture;

public abstract class Map {
	
	protected Texture base_map;
	protected BufferedImage truecolor;

	protected ArrayList<Texture> territory_textures = new ArrayList<>();	
	protected ArrayList<Territory> territories = new ArrayList<Territory>();
	
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
	
	public int getTerritoryClicked(int xpos, int ypos) {
		int rgb = truecolor.getRGB(xpos, ypos);
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;
		return findTerritory(red, green, blue);
	}
	
	//load an image from file path
	public BufferedImage loadImage(String path) {
		File in = new File(path);
		BufferedImage im;
		try {
			im = ImageIO.read(in);
			return im;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void bindTexture() {
		base_map.bind(0);
	}
	
	public ArrayList<Texture> getTerritoryTextures(){
		return territory_textures;
	}
	
	public ArrayList<Territory> getTerritories(){
		return territories;
	}
	
	public void setTerritories(ArrayList<Territory> newterritories) {
		territories = newterritories;
	}

}
