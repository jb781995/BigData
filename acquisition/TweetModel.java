package edu.csula.datascience.acquisition;

public class TweetModel {
	
	private String username;
	private String profileLocation;
	private long tweetId;
	private String content;
	
	public TweetModel(String username, String profileLocation, long tweetId,
			String content) {
		super();
		this.username = username;
		this.profileLocation = profileLocation;
		this.tweetId = tweetId;
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfileLocation() {
		return profileLocation;
	}

	public void setProfileLocation(String profileLocation) {
		this.profileLocation = profileLocation;
	}

	public long getTweetId() {
		return tweetId;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
