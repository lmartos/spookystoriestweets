package nl.saxion.spookystoriestweets.model;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
import org.json.JSONException;
import org.json.JSONObject;

public class Hashtag extends Entity {

	private String text;

	/**
	 * 			gets the text of a Hashtag
	 * @return	return the hashtag in String
	 */
	public String getText() {
		return text;
	}
	/**
	 * 						retrieves the text of a hashtag from a JSONObject
	 * @param entityObj
	 * @throws JSONException
	 */
	public Hashtag(JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		text = entityObj.getString("text");
	}
}
