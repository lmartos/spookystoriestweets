package nl.saxion.spookystoriestweets.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Model extends Observable implements Observer {
	
	private ArrayList<Tweet> Tweets = new ArrayList<Tweet>();
	private ArrayList<Tweet> timeline = new ArrayList<Tweet>();
	private String json = "";
	private OAuthConsumer consumer;
	private OAuthProvider provider;
	private boolean loggedIn = false;
	
	public void setJson(String json) {
		this.json = json;
		buildTweetList();
		setChanged();
		notifyObservers();
	}
	
	public void setJsonTimeline(String json) {
		this.json = json;
		buildTimeline();
		setChanged();
		notifyObservers();
	}
	
	public void setConsumer(OAuthConsumer consumer){
		this.consumer = consumer;
		this.loggedIn = true;
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn){
		this.loggedIn = loggedIn;
	}
	public void setProvider(OAuthProvider provider){
		this.provider = provider;
	}
	
	public OAuthConsumer getConsumer(){
		return this.consumer;
	}
	
	public OAuthProvider getProvider(){
		return this.provider;
	}
	
	public void buildTweetList() {
	
		Tweets.clear();
		try {
			JSONObject result = new JSONObject(this.json);
			JSONArray jsonStatus = result.getJSONArray("statuses");
			
			for (int i = 0; i < jsonStatus.length(); i++) {
				addTweet(new Tweet(jsonStatus.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void buildTimeline() {
		
		timeline.clear();
		try {
			JSONObject result = new JSONObject(this.json);
			JSONArray jsonStatus = result.getJSONArray("statuses");
			
			for (int i = 0; i < jsonStatus.length(); i++) {
				addToTimeline(new Tweet(jsonStatus.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	private void addTweet(Tweet tweet) {
		tweet.addObserver(this);
		Tweets.add(tweet);
	}
	
	public void addToTimeline(Tweet tweet){
		this.timeline.add(tweet);
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<Tweet> getTimeline(){
		return this.timeline;
	}
	
	public ArrayList<Tweet> getTweets() {
		return this.Tweets;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}

	public void clearTimeline(){
		this.timeline.clear();
	}
}
