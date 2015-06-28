package nl.saxion.spookystoriestweets.model;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class Entity {
	private ArrayList<Integer> indices = new ArrayList<Integer>();

	public Entity(JSONArray indicesJSON) throws JSONException {
		for (int i = 0; i < indicesJSON.length(); i++) {
			indices.add(indicesJSON.getInt(i));
		}
	}
	
	public Entity(int start, int end) {
		indices.add(start);
		indices.add(end);
	}
	/**
	 * 				returns the indices in Integer
	 * @param index
	 * @return
	 */
	public int getIndex(int index) {
		assert index == 1 || index == 2 : "Wrong indices";
		if (index == 1)
			return indices.get(0);
		else
			return indices.get(1);
	}

}
