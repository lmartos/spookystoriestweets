package nl.saxion.spookystoriestweets;

/**	The Application class for The Twitterclient
 * 	used for retrieving the model for the application
 * 
 * @author Doron Hartog & Laurens Martos
 *
 */

import android.app.Application;
import nl.saxion.spookystoriestweets.model.Model;

public class SpookyStoriesTweetsApplication extends Application {
	private Model model;

	public void onCreate() {
		model = new Model();
	}

	/**
	 * getter for the Model
	 * 
	 * @return returns the model of the application
	 */
	public Model getModel() {
		return this.model;
	}
}
