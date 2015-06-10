package nl.saxion.spookystoriestweets;

import java.util.Observable;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class User extends Observable {
	private String userName;
	private String screenName;
	private String pictureURL;
	//private Bitmap screenPicture;
	
	public User(JSONObject userObject) throws JSONException {
		userName = userObject.getString("name");
		screenName = userObject.getString("screen_name");
		pictureURL =  userObject.getString("profile_image_url");
	}
	
	public String getScreenName() {
		return this.screenName;
	}
	
}
