package edu.csula.datascience.acquisition;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import twitter4j.Status;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

//Implemented Stackoverflow collector to save records in mongodb
public class StackoverflowCollector implements
		StackOverflowCollectorInterface<Status, Status> {

	MongoClient mongoClient;
	MongoDatabase database;
	static MongoCollection<Document> collection;

	public StackoverflowCollector() {
		mongoClient = new MongoClient();
		database = mongoClient.getDatabase("GoldenEagle-BigData");
		collection = database.getCollection("Stackoverflow-BigData");
	}

	@Override
	public void save(HttpResponse<JsonNode> data) {
		// TODO Auto-generated method stub
		Document myDoc = Document.parse(data.getBody().getObject().toString(2));
		collection.insertOne(myDoc);

	}

	@Override
	public HttpResponse<JsonNode> mungee(HttpResponse<JsonNode> records) {

		JSONArray cleanedArray = new JSONArray();
		JSONArray array = records.getBody().getObject()
				.getJSONArray("items");
        if(array !=null && array.length()>0)
        {
		for (int i = 0; i < array.length(); i++) {

			JSONObject tagObj = array.getJSONObject(i);
    //Going to remove irrelevant data and null data
			if (!tagObj.get("tags").toString().contains("java") && tagObj.get("tags").toString()!="")
				{
				array.remove(i);
			} else {
				cleanedArray.put(array.getJSONObject(i));
			}
		}
        }

		return records;
	}
}
