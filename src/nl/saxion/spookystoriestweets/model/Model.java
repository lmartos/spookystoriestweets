package nl.saxion.spookystoriestweets.model;

import java.util.ArrayList;

import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nl.saxion.spookystoriestweets.Tweet;

public class Model extends Observable implements Observer {
	
	private ArrayList<Tweet> listOfTweets = new ArrayList<Tweet>();
	private String json = "";
	
	public void setJson(String _json) {
		this.json = _json;
		buildTweetList();
		setChanged();
		notifyObservers();
	}
	
	public void buildTweetList() {
		// Maak de lijst leeg zodat je alleen de nieuwe gegevens ziet
		listOfTweets.clear();
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
		listOfTweets.add(tweet);
	}
	
	public ArrayList<Tweet> getTweets() {
		return this.listOfTweets;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}

}
