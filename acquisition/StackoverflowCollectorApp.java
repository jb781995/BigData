package edu.csula.datascience.acquisition;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class StackoverflowCollectorApp {

	public static void main(String[] args) {

		//Implemented code to collect the records like question,answer,tag from stackoverflow
		StackoverflowSource source = new StackoverflowSource("java", 1, 25);
		StackoverflowCollector collector = new StackoverflowCollector();

		do {
			HttpResponse<JsonNode> results = null;
			try {
				results = source.next();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpResponse<JsonNode> cleanedRecords = collector.mungee(results);//Calling mungee to remove dirtyRecords
			collector.save(cleanedRecords);
		} while (source.hasNext());
	}
}
