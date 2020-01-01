package edu.csula.datascience.acquisition;

import java.util.Collection;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class LiveTweetsSource implements Source<Status>{

	int counter = 0;
	LiveTweetsCollector data = new LiveTweetsCollector();

	// implementation of 'next' for live stream
	public void getTweets() {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		
		//provide your own keys
	    cb.setOAuthConsumerKey("ConsumerKey");
	    cb.setOAuthConsumerSecret("ConsumerSecret");
	    cb.setOAuthAccessToken("AccessToken");
	    cb.setOAuthAccessTokenSecret("TokenSecret");

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();        
        
        //Check for live status updates
        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
            	System.out.println("Exception!! Data Not Inserted Into Database");
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
            	System.out.println("Got a status deletion notice id:" + arg0.getStatusId());
            }
            
            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }
            
            @Override
            public void onStatus(Status status) {
            	
            	 User user = status.getUser();
            	 
                 String username = status.getUser().getScreenName();
                 String profileLocation = user.getLocation();
                 long tweetId = status.getId(); 
                 String content = status.getText();
                 
                  
                //Create a model for the live data
                TweetModel memoryData = new TweetModel(username, profileLocation, tweetId, content);
                
                //store to MongoDB if data is correct
                System.out.println(++counter + ") username: " + username + " location: " + profileLocation + " tweetId " + tweetId + " Text: " + content );
                data.save(memoryData);
            }
            
			@Override
            public void onTrackLimitationNotice(int arg0) {
	            System.out.println("Got track limitation notice:" + arg0);
			}
			
			@Override
			public void onStallWarning(StallWarning warning) {}
        };
        
        //Filter the live tweet results
        FilterQuery filterQuery = new FilterQuery();
        //Search for tweets with specific keywords
        String keywords[] = {"Java", "Python", "PHP"};
        //Restrict the language to English
        String[] lang = {"en"};  
        //Add the Filters
        filterQuery.language(lang);
        filterQuery.track(keywords);
        //Listen for Live Tweets
        twitterStream.addListener(listener);
        twitterStream.filter(filterQuery);
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Collection<Status> next() {
		return null;
	}
}
