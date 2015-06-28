package nl.saxion.spookystoriestweets.model;

/**	The User Class for the twitterclient	
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */
import java.util.Observable;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;

public class User extends Observable {
	private String name;
	private String screenName;
	private String pictureURL;
	private String profileBannerURL;

	private int followers;
	private int send_tweets;
	private int following;

	private Bitmap screenPicture;
	private String user_id;

	public User(JSONObject userObject) throws JSONException {
		name = userObject.getString("name");
		screenName = userObject.getString("screen_name");
		pictureURL = userObject.getString("profile_image_url");
		followers = userObject.getInt("followers_count");
		following = userObject.getInt("friends_count");
		send_tweets = userObject.getInt("statuses_count");
		user_id = userObject.getString("id_str");
		profileBannerURL = userObject.getString("profile_background_image_url");

	}

	/**
	 * The getter of the full name of a user
	 * 
	 * @return Returns the full name in String
	 */
	public String getName() {
		return name;
	}

	/**
	 * The getter of the screenname of a user
	 * 
	 * @return Returns the screenname in String
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * The getter of the avatar of an user
	 * 
	 * @return Returns the picture url in String
	 */
	public String getPictureURL() {
		return pictureURL;
	}

	/**
	 * The setter of a user Picture
	 */
	public void setUserPicture(Bitmap result) {
		screenPicture = result;
		setChanged();
		notifyObservers();
	}

	/**
	 * The getter of the picture of a user
	 * 
	 * @return returns the screenPicture in bitmap
	 */
	public Bitmap getScreenPicture() {
		return screenPicture;
	}

	/**
	 * The getter of the followers of a user
	 * 
	 * @return Returns the amount of followers in Integer
	 */
	public int getFollowers() {
		return followers;
	}

	/**
	 * The getter for the amount of following of a user
	 * 
	 * @return Returns the amount of following in Integer
	 */
	public int getFollowing() {
		return following;
	}

	/**
	 * The getter of the amount of tweets of a user
	 * 
	 * @return Returns the amount of tweets in Integer
	 */
	public int getSend_tweets() {
		return send_tweets;
	}

	/**
	 * The getter of the user_id of a user
	 * 
	 * @return Returns the user_id in Integer
	 */
	public String getUserId() {
		return user_id;
	}

	/**
	 * retrieves the user information from a JSONObject
	 * 
	 * @param userObject
	 *            the JSONObject from a user
	 * @throws JSONException
	 */
	public void updateUser(JSONObject userObject) throws JSONException {
		name = userObject.getString("name");
		screenName = userObject.getString("screen_name");
		pictureURL = userObject.getString("profile_image_url");
		followers = userObject.getInt("followers_count");
		following = userObject.getInt("following");
		send_tweets = userObject.getInt("statuses_count");

		setChanged();
		notifyObservers();
	}

	/**
	 * The getter of the profile banner url of a user
	 * 
	 * @return Returns the profile banner url in String
	 */
	public String getProfileBannerURL() {
		return profileBannerURL;
	}

}
