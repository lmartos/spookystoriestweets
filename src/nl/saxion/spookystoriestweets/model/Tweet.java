package nl.saxion.spookystoriestweets.model;

/**	
 * 
 * @author Doron Hartog & Laurens Martos

 *
 */
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Observable;

public class Tweet extends Observable implements Observer {

	private String tweetedText, tweetDate;
	private User user;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private JSONArray hashtags, urls, media;
	private int tweetId;
	/**
	 * 					The constructor of a Tweet
	 * 					Builds the Tweet from a JSONObject
	 * @param tweetObj
	 * @throws JSONException
	 */
	public Tweet(JSONObject tweetObj) throws JSONException {
		user = new User(tweetObj.getJSONObject("user"));
		user.addObserver(this);
		tweetedText = tweetObj.getString("text");
		tweetDate = tweetObj.getString("created_at");
		tweetId = tweetObj.getInt("id");

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
	/**
	 * 			the getter of the complete tweet
	 * @return	returns the tweet in String
	 */
	public String getTweetText() {
		return this.tweetedText;
	}

	/**
	 * 	updates the data
	 */
	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}
	/**
	 * 			the getter of the Entities
	 * @return	returns an ArrayList<Entity>
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	/**
	 * 			the getter of a user
	 * @return	returns the user in User
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * 			the getter of a twitter id
	 * @return	returns the tweetId in Integer
	 */
	public int getId() {
		return this.tweetId;
	}
}
