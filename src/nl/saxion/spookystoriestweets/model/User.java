package nl.saxion.spookystoriestweets.model;

import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class User extends Observable {
	private String userName;
	private String screenName;
	private String pictureURL;

	
	public User(JSONObject userObject) throws JSONException {
		userName = userObject.getString("name");
		screenName = userObject.getString("screen_name");
		pictureURL =  userObject.getString("profile_image_url");
	}
	
	public User(twitter4j.User user){
		this.userName = user.getName();
		this.screenName = user.getScreenName();
		this.pictureURL = user.getProfileImageURL();
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getScreenName() {
		return this.screenName;
	}
	
}
