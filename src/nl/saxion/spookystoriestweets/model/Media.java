package nl.saxion.spookystoriestweets.model;

public class Media {
	private String media_url;
	private String display_url;
	private int indices [];
	
	public Media(String media_url, String display_url, int indices []){
		this.media_url = media_url;
		this.display_url = display_url;
		this.indices = indices;
	}
}
