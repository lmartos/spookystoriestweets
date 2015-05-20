package nl.saxion.spookystoriestweets.model;

public class Tweet {
	
	private String name;
	private String tag;
	private String text;
	private String timestamp;
	private String avatarURL;
	private String retweetedBy;
	private int retweets;
	private int favorites;
	
	public Tweet (String name, String tag, String text, String timestamp, String avatarURL, int retweets, int favorites){
		this.name = name;
		this.tag = tag;
		this.text = text;
		this.timestamp = timestamp;
		this.retweets = retweets;
		this.favorites = favorites;
		this.avatarURL = avatarURL;
		
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setRetweetedBy(String retweetedBy){
		this.retweetedBy = retweetedBy;
	}
	
	public String getRetweetedBy(){
		return this.retweetedBy;
	}
	
	public String getAvatarURL(){
		return avatarURL;
	}
	
	public String getText(){
		return this.text;
	}
	
	public String getTimeStamp(){
		return this.timestamp;
	}
	
	public String getTag(){
		return this.tag;
	}

}
