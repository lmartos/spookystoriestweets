package nl.saxion.spookystoriestweets.model;

/**	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;

public class Media extends Entity {
	private String media_url, display_url = "";
	private Bitmap image;
	
	/**
	 * 			getter for the media url
	 * @return	return the media url in String
	 */
	public String getMediaURL() {
		return this.media_url;
	}
	
	/**
	 * 			the getter for the media in Bitmap
	 * @return	returns the image in Bitmap
	 */
	public Bitmap getPicture() {
		return this.image;
	}
	public void setPicture(Bitmap image) {
		this.image = image;
	}

	/**
	 * 					the constructor of media creates the media_url and display_url from a JSONObject
	 * @param entityObj
	 * @throws JSONException
	 */
	public Media(JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		media_url = entityObj.getString("media_url");
		display_url = entityObj.getString("display_url");
	}
}
