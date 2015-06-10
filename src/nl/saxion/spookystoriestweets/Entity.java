package nl.saxion.spookystoriestweets;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class Entity {
	private ArrayList<Integer> indices = new ArrayList<Integer>();
	
	public Entity (JSONArray indicesJSON) throws JSONException {
		for (int i = 0; i < indicesJSON.length(); i++) {
			indices.add(indicesJSON.getInt(i));
		}
	}
	
	/**
	 * getIndex returns the index of the entity
	 * @param index select the first or the second index
	 * @return the selected index
	 */
	public int getIndex(int index) {
		assert index == 1 || index == 2 : "Geen juiste index meegegeven";
		if (index == 1)
			return indices.get(0);
		else
			return indices.get(1);
	}
}
