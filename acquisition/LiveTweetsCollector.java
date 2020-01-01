package edu.csula.datascience.acquisition;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import twitter4j.FilterQuery;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.csula.datascience.acquisition.Collector;

public class LiveTweetsCollector implements Collector<Status, Status>{
	
    	MongoClient mongoClient;
    	MongoDatabase database;
    	static MongoCollection<Document> collection;
    	List<TweetModel> listOfAllTweets;
    
	public LiveTweetsCollector() {
        	// establish database connection to MongoDB
        	mongoClient = new MongoClient();

	        // select `GoldenEagle-BigData` as the database
        	database = mongoClient.getDatabase("GoldenEagle-BigData");

        	// select collection by name `Stream-Tweets`
        	collection = database.getCollection("Stream-Tweets");
        
        	// return a list of all tweets
        	listOfAllTweets = new ArrayList<TweetModel>();
    	}
    
	@Override
	public List<TweetModel> mungee(List<TweetModel> src) {
		
		String pattern = "(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})";

		for (int i = 0; i < src.size(); i++) {
			//Remove record if field contains null
			if (src.get(i).getContent() == null || src.get(i).getProfileLocation() == null 
					|| src.get(i).getUsername() == null ) {
				src.remove(i);
			}
			
			// Check if the Data contains only links
			// The regex will be updated to remove special characters and more
			if (src.get(i).getContent().matches(pattern)) {
				src.remove(i);
			}
		}
		
		return src;		
	}

	@Override
	public void save(TweetModel data) {
		//save a single document to MongoDB
		Document document = new Document();
	    	document.put("TweetId", data.getTweetId());
			document.put("Username", data.getUsername());
			document.put("ProfileLocation", data.getProfileLocation());
			document.put("Content", data.getContent());
		collection.insertOne(document);
		//write data to json file
		// WriteToJson(data);
	}

	private void WriteToJson(TweetModel data) {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("Content", data.getContent());
			obj.put("TweetId", data.getTweetId());
			obj.put("Username", data.getUsername());
			obj.put("ProfileLocation", data.getProfileLocation());
		} catch (JSONException e) {
			e.printStackTrace();
		}
 
		try (FileWriter file = new FileWriter("datadump.json", false)) {
			file.append(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	//return list of all tweets
	@Override
	public List<TweetModel> getAllTweets() {
		return listOfAllTweets;
	}
}
