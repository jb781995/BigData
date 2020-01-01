package edu.csula.datascience.acquisition;

public class LiveTweetsCollectorApp {

	public static void main(String[] args) {
		
		LiveTweetsSource tweets = new LiveTweetsSource();
		tweets.getTweets();
		
	}

}
