package nl.saxion.spookystoriestweets;

import android.app.Application;

import nl.saxion.spookystoriestweets.model.Model;

public class SpookyStoriesTweetsApplication extends Application {
	Model model = new Model();
	
	public Model getModel() {
		return this.model;
	}
}
