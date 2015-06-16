package nl.saxion.spookystoriestweets.model;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.URLEntity;

public class Url extends Entity {
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	public Url (JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		url = entityObj.getString("url");
	}
	
	public Url (URLEntity entity){
		super(entity.getStart(), entity.getEnd());
		url = entity.getURL();
	}

}
