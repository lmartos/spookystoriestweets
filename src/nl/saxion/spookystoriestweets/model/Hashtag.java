package nl.saxion.spookystoriestweets.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.HashtagEntity;

public class Hashtag extends Entity {

	private String text;

	public String getText() {
		return text;
	}
	
	public Hashtag(JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		text = entityObj.getString("text");
	}
	
	public Hashtag(HashtagEntity entity){
		super(entity.getStart(), entity.getEnd());
		text = entity.getText();
	}

}
