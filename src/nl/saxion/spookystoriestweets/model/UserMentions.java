package nl.saxion.spookystoriestweets.model;

public class UserMentions {
	private String screen_name;
	private String name;
	private int indices[];
	
	public UserMentions(String screen_name, String name, int indices[]){
		this.screen_name = screen_name;
		this.name = name;
		this.indices = indices;
	}
}
