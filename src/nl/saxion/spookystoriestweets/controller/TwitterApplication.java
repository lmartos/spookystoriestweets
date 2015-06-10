package nl.saxion.spookystoriestweets.controller;

import android.app.Application;

import nl.saxion.spookystoriestweets.model.Model;

public class TwitterApplication extends Application {
	Model model = new Model();
	
	public Model getModel() {
		return this.model;
	}
}
