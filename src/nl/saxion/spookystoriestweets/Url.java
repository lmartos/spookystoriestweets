package nl.saxion.spookystoriestweets;

import org.json.JSONException;
import org.json.JSONObject;

public class Url extends Entity {
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	public Url (JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		url = entityObj.getString("url");
	}

}
