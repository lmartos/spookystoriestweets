package nl.saxion.spookystoriestweets;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

public class Tweet extends Observable implements Observer{

	private String tweetText, tweetDate;
	private User user;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private JSONArray hashtags, urls, media;
	
	public Tweet (JSONObject tweetObj) throws JSONException {
		user = new User(tweetObj.getJSONObject("user"));
		user.addObserver(this);
		tweetText = tweetObj.getString("text");
		tweetDate = tweetObj.getString("created_at");
		
		/** Entities uitlezen */
		JSONObject entityObj = tweetObj.getJSONObject("entities");
		
		/** JSONArrays opbouwen */
		if (entityObj.has("hashtags")) {
			hashtags = entityObj.getJSONArray("hashtags");
			for (int i = 0; i < hashtags.length(); i++) {
				entities.add(new Hashtag(hashtags.getJSONObject(i)));
			}
		}
		if (entityObj.has("urls")) {
			urls = entityObj.getJSONArray("urls");
			for (int i = 0; i < urls.length(); i++) {
				entities.add(new Url(urls.getJSONObject(i)));
			}
		}
		if (entityObj.has("media")) {
			media = entityObj.getJSONArray("media");
			for (int i = 0; i < media.length(); i++) {
				entities.add(new Media(media.getJSONObject(i)));
			}
		}		
	}
	
	public String getTweetText() {
		return this.tweetText;
	}

	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public User getUser() {
		return this.user;
	}


}
