package packets;
import java.util.ArrayList;
import mainframe.Unit;

public class UnitPositions {
	
	private ArrayList<UnitInfo> unitdata = new ArrayList<>();
	
	public UnitPositions(){
		
	}
	
	public UnitPositions(ArrayList<Unit> units){
		for (int i = 0; i < units.size(); i++) {
			Unit current = units.get(i);
			unitdata.add(new UnitInfo(current.getOwnerId(), current.getLocation()));
		}
	}

	public ArrayList<UnitInfo> getUnitdata() {
		return unitdata;
	}

}
