package nl.saxion.spookystoriestweets.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Model extends Observable implements Observer {
	
	private ArrayList<Tweet> Tweets = new ArrayList<Tweet>();
	private String json = "";
	
	public void setJson(String json) {
		this.json = json;
		buildTweetList();
		setChanged();
		notifyObservers();
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
	
	private void addTweet(Tweet tweet) {
		tweet.addObserver(this);
		Tweets.add(tweet);
	}
	
	public ArrayList<Tweet> getTweets() {
		return this.Tweets;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}

}
