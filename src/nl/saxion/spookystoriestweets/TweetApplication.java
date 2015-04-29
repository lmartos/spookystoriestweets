package nl.saxion.spookystoriestweets;

import nl.saxion.spookystoriestweets.model.Model;
import android.app.Application;

public class TweetApplication extends Application{
	
	private Model model;
	
	public void onCreate(){
		model = new Model();
	}
	
	public Model getModel(){
		return model;
	}

}
