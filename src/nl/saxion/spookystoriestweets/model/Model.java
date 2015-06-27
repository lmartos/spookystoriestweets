package nl.saxion.spookystoriestweets.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Model extends Observable implements Observer {
	
	private ArrayList<Tweet> Tweets = new ArrayList<Tweet>();
	private ArrayList<Tweet> timeline = new ArrayList<Tweet>();
	private ArrayList<Tweet> followerTimeline = new ArrayList<Tweet>();
	private ArrayList<User> followers = new ArrayList<User>();
	private String json = "";
	private OAuthConsumer consumer;
	private OAuthProvider provider;
	private User loggedInUser;
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
	
	public void createLoggedInUser(String userJSON){
		
		try {
			
			JSONObject userData = new JSONObject(userJSON);
			this.loggedInUser = new User(userData);
			Log.d("successfully created user", loggedInUser.getName());
		} catch (JSONException e) {
			Log.d("user creation error", "unable to build user from current json data");
			e.printStackTrace();
		}
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
	

	
	public ArrayList<User> getFollowerList(){
		return followers;
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
				timeline.add(new Tweet(jsonStatus.getJSONObject(i)));
				setChanged();
				notifyObservers();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void buildFollowerTimeline(String json) {
		
		followerTimeline.clear();
		try {
			JSONObject result = new JSONObject(json);
			JSONArray jsonStatus = result.getJSONArray("statuses");
			
			for (int i = 0; i < jsonStatus.length(); i++) {
				followerTimeline.add(new Tweet(jsonStatus.getJSONObject(i)));
				setChanged();
				notifyObservers();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Tweet> getFollowerTimeline(){
		return this.followerTimeline;
	}
	
	
	private void addTweet(Tweet tweet) {
		tweet.addObserver(this);
		Tweets.add(tweet);
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

	public User getLoggedInUser() {
		return this.loggedInUser;
	}

	public void handleFollowers(String result) {
		try {
			JSONObject followersJSON = new JSONObject(result);
			JSONArray followersJSONArray = followersJSON.getJSONArray("users");
			for (int i = 0; i < followersJSONArray.length(); i++) {
				followers.add(new User(followersJSONArray.getJSONObject(i)));
			}
			setChanged();
			notifyObservers();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
