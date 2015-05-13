package nl.saxion.spookystoriestweets.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {
	
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private String filename;
	
	public Model(){
		tweets.add(new Tweet("job", " ", "dit is een test", " ", 0 , 0));
	}
	
	public void addTweet(Tweet tweet){
		tweets.add(tweet);
		
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<Tweet> getData(){
		return tweets;
	}
	
	

	
}
