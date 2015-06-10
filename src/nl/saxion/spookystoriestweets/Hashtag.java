package nl.saxion.spookystoriestweets;

import org.json.JSONException;
import org.json.JSONObject;

public class Hashtag extends Entity {

	private String text;

	public String getText() {
		return text;
	}
	
	public Hashtag(JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		text = entityObj.getString("text");
	}

}
