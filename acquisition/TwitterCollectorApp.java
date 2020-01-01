package edu.csula.datascience.acquisition;

import twitter4j.Status;

import java.util.Collection;

/**
 * A simple example of using Twitter
 */
public class TwitterCollectorApp {
    public static void main(String[] args) {
    	
        TwitterSource source = new TwitterSource(Long.MAX_VALUE, "#java");
        
        TwitterCollector collector = new TwitterCollector();
        int counter = 0;
        while (source.hasNext()) {
            Collection<Status> tweets = source.next();
            
            System.out.println(++counter + " )  "+tweets.toString());
            
 //           Collection<Status> cleanedTweets = collector.mungee(tweets);
 //           collector.save(cleanedTweets);           
        }
    }
}
