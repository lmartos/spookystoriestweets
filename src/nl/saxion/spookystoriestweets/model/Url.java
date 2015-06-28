package nl.saxion.spookystoriestweets.model;

/**	The Url class for the twitterclient
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
import org.json.JSONException;
import org.json.JSONObject;

public class Url extends Entity {
	private String url;

	/**
	 * the getter for the url
	 * 
	 * @return returns the url in String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * the constructor for the URL
	 * 
	 * @param entityObj
	 * @throws JSONException
	 */
	public Url(JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		url = entityObj.getString("url");
	}
}
