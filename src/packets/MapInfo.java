package packets;
import java.util.ArrayList;

import mainframe.Territory;
import mainframe.Unit;

public class MapInfo {
	
	private ArrayList<TerritoryInfo> territories = new ArrayList<>();
	
	public MapInfo(){
		
	}
	
	public MapInfo(ArrayList<Territory> newterritories){
		for (int i = 0; i < newterritories.size(); i++) {
			Territory current = newterritories.get(i);
			territories.add(new TerritoryInfo(current.getId(), current.getOwner()));
		}
	}

	public ArrayList<TerritoryInfo> getTerritories() {
		return territories;
	}

}
