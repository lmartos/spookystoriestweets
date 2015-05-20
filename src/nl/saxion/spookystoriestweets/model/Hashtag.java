package nl.saxion.spookystoriestweets.model;

public class Hashtag extends Entity{

	private String text;
	private int indices [];
	
	public Hashtag(String text, int indices []){
		this.text = text;
		this.indices = indices;
	}
}
