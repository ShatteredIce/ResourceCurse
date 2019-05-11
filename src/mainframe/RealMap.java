package mainframe;
import java.awt.image.BufferedImage;

import rendering.Texture;

public class RealMap extends Map {

	public RealMap() {
		base_map = new Texture("finalmap.png");

		truecolor = loadImage("./res/finalmap_truecol.png");


		territories.add(new Territory(0, 0, 255, 0, 0, 360, 230, 0, 1, new int[]{1,2}));
		territories.add(new Territory(1, 0, 235, 0, 0, 480, 410, 2, 1, new int[]{0,2,3,4}));
		territories.add(new Territory(2, 0, 215, 0, 0, 430, 580, 1, 2,new int[]{1,3,7}));
		territories.add(new Territory(3, 0, 195, 0, 0, 600, 600, 2, 2, new int[]{1,2,4,6,7}));
		territories.add(new Territory(4, 0, 175, 0, 0, 640, 380, 1, 1,new int[]{1,3,5,6}));
		territories.add(new Territory(5, 0, 155, 0, 0, 670, 260, 0, 2,new int[]{4,6}));
		territories.add(new Territory(6, 0, 135, 0, 0, 770, 360, 2, 1,new int[]{3,4,5,7,9,10}));
		territories.add(new Territory(7, 0, 115, 0, 0, 810, 590, 1, 1,new int[]{2,3,6,8,10,11,12,14,15,16}));
		territories.add(new Territory(8, 0, 95, 0, 0, 710, 740, 0, 2,new int[]{7,11}));
		territories.add(new Territory(9, 0, 75, 0, 0, 860, 245, 2, 1,new int[]{6,10}));
		territories.add(new Territory(10, 0, 55, 0, 0, 945, 400, 1, 1,new int[]{6,7,9,13,14}));
		territories.add(new Territory(11, 0, 35, 0, 0, 820, 750, 2, 2,new int[]{7,8,12}));
		territories.add(new Territory(12, 0, 15, 0, 0, 920, 755, 1, 2,new int[]{7,11,16}));
		territories.add(new Territory(13, 0, 0, 255, 0, 1080, 230, 0, 1,new int[]{10,14,18}));
		territories.add(new Territory(14, 0, 0, 235, 0, 1160, 405, 2, 1,new int[]{7,10,13,15,18,19,20}));
		territories.add(new Territory(15, 0, 0, 215, 0, 1130, 665, 2, 2,new int[]{7,14,16,17,20,21}));
		territories.add(new Territory(16, 0, 0, 195, 0, 1010, 710, 1, 2,new int[]{7,12,15,17}));
		territories.add(new Territory(17, 0, 0, 175, 0, 1105, 815, 0, 2,new int[]{15,16,21}));
		territories.add(new Territory(18, 0, 0, 155, 0, 1210, 215, 0, 1,new int[]{13,14,19,23}));
		territories.add(new Territory(19, 0, 0, 135, 0, 1270, 300, 1, 1,new int[]{14,18,20,23,26,28}));
		territories.add(new Territory(20, 0, 0, 115, 0, 1335, 580, 2, 1,new int[]{14,15,19,21,22,28,32,33,34}));
		territories.add(new Territory(21, 0, 0, 95, 0, 1210, 815, 1, 1,new int[]{15,17,20,22}));
		territories.add(new Territory(22, 0, 0, 75, 0, 1265, 885, 2, 2,new int[]{20,21}));
		territories.add(new Territory(23, 0, 0, 55, 0, 1325, 200, 0, 2,new int[]{18,19,24,26}));
		territories.add(new Territory(24, 0, 0, 35, 0, 1395, 235, 1, 2,new int[]{23,25,26}));
		territories.add(new Territory(25, 0, 0, 15, 0, 1450, 210, 0, 1,new int[]{24,26,27}));
		territories.add(new Territory(26, 0, 0, 0, 255, 1375, 345, 0, 2,new int[]{19,23,24,25,27,28}));
		territories.add(new Territory(27, 0, 0, 0, 235, 1480, 305, 1, 2,new int[]{25,26,28}));
		territories.add(new Territory(28, 0, 0, 0, 215, 1465, 475, 2, 1,new int[]{19,20,26,27,29,30,31,32}));
		territories.add(new Territory(29, 0, 0, 0, 195, 1580, 395, 2, 1,new int[]{28,30}));
		territories.add(new Territory(30, 0, 0, 0, 175, 1640, 445, 1, 2,new int[]{28,29,31}));
		territories.add(new Territory(31, 0, 0, 0, 155, 1560, 505, 0, 2,new int[]{28,30,32}));
		territories.add(new Territory(32, 0, 0, 0, 135, 1585, 585, 1, 1,new int[]{20,28,31,33,35}));
		territories.add(new Territory(33, 0, 0, 0, 115, 1540, 665, 2, 1,new int[]{20,32,34,35}));
		territories.add(new Territory(34, 0, 0, 0, 95, 1515, 725, 0, 1,new int[]{20,33,35}));
		territories.add(new Territory(35, 0, 0, 0, 75, 1695, 695, 1, 1,new int[]{32,33,34}));
		
		
		territory_textures.add(new Texture("territory1.PNG"));
		territory_textures.add(new Texture("territory2.PNG"));
		territory_textures.add(new Texture("territory3.PNG"));
		territory_textures.add(new Texture("territory4.PNG"));
		territory_textures.add(new Texture("territory5.PNG"));
		territory_textures.add(new Texture("territory6.PNG"));
		territory_textures.add(new Texture("territory7.PNG"));
		territory_textures.add(new Texture("territory8.PNG"));
		territory_textures.add(new Texture("territory9.PNG"));
		territory_textures.add(new Texture("territory10.PNG"));
		territory_textures.add(new Texture("territory11.PNG"));
		territory_textures.add(new Texture("territory12.PNG"));
		territory_textures.add(new Texture("territory13.PNG"));
		territory_textures.add(new Texture("territory14.PNG"));
		territory_textures.add(new Texture("territory15.PNG"));
		territory_textures.add(new Texture("territory16.PNG"));
		territory_textures.add(new Texture("territory17.PNG"));
		territory_textures.add(new Texture("territory18.PNG"));
		territory_textures.add(new Texture("territory19.PNG"));
		territory_textures.add(new Texture("territory20.PNG"));
		territory_textures.add(new Texture("territory21.PNG"));
		territory_textures.add(new Texture("territory22.PNG"));
		territory_textures.add(new Texture("territory23.PNG"));
		territory_textures.add(new Texture("territory24.PNG"));
		territory_textures.add(new Texture("territory25.PNG"));
		territory_textures.add(new Texture("territory26.PNG"));
		territory_textures.add(new Texture("territory27.PNG"));
		territory_textures.add(new Texture("territory28.PNG"));
		territory_textures.add(new Texture("territory29.PNG"));
		territory_textures.add(new Texture("territory30.PNG"));
		territory_textures.add(new Texture("territory31.PNG"));
		territory_textures.add(new Texture("territory32.PNG"));
		territory_textures.add(new Texture("territory33.PNG"));
		territory_textures.add(new Texture("territory34.PNG"));
		territory_textures.add(new Texture("territory35.PNG"));
		territory_textures.add(new Texture("territory36.PNG"));
		
	}

}
