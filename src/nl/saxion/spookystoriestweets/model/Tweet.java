package nl.saxion.spookystoriestweets.model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.util.Observable;

public class Tweet extends Observable implements Observer{

	private String tweetedText, tweetDate;
	private User user;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private JSONArray hashtags, urls, media;
	
	public Tweet (JSONObject tweetObj) throws JSONException {
		user = new User(tweetObj.getJSONObject("user"));
		user.addObserver(this);
		tweetedText = tweetObj.getString("text");
		tweetDate = tweetObj.getString("created_at");
		
		
		JSONObject entityObj = tweetObj.getJSONObject("entities");
		
	
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
	
	public Tweet(Status status){
		this.user = new User(status.getUser());
		user.addObserver(this);
		this.tweetedText = status.getText();
		this.tweetDate = status.getCreatedAt().toString();
		
		
		
		if(status.getHashtagEntities().length > 0){
			for(HashtagEntity h : status.getHashtagEntities()){
				entities.add(new Hashtag(h));
			}

		}
		
		if(status.getURLEntities().length > 0){
			for(URLEntity u: status.getURLEntities()){
				entities.add(new Url(u));
			}
		}
		
		if(status.getMediaEntities().length > 0){
			for(MediaEntity m : status.getMediaEntities()){
				
			}
		}
		
	}
	
	public String getTweetText() {
		return this.tweetedText;
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
