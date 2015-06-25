package nl.saxion.spookystoriestweets;

import oauth.signpost.OAuth;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import nl.saxion.spookystoriestweets.model.Model;

public class SpookyStoriesTweetsApplication extends Application {
	private Model model;
	
	public void onCreate(){
		model = new Model();
	}
	
	public Model getModel() {
		return this.model;
	}
}
