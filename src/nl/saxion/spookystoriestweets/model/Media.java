package nl.saxion.spookystoriestweets.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Media extends Entity {
	private String media_url, display_url = ""; 
	private Bitmap image;
	
	public String getMediaURL() {
		return this.media_url;
	}
	public Bitmap getPicture() {
		return this.image;
	}
	public void setPicture(Bitmap image) {
		this.image = image;
	}
	
	public Media (JSONObject entityObj) throws JSONException {
		super(entityObj.getJSONArray("indices"));
		media_url = entityObj.getString("media_url");
		display_url = entityObj.getString("display_url");
	}
}
