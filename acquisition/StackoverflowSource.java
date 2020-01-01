package edu.csula.datascience.acquisition;

import twitter4j.Status;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

//implemented stackoverflowsource
public class StackoverflowSource implements
		StackOverflowSourceInterface<Status> {

	private final String searchQuery;
	boolean recordsAvailable;
	int pagesize;
	int page;

	public StackoverflowSource(String query, int page, int pagesize) {
		this.searchQuery = query;
		this.page = page;
		this.pagesize = pagesize;
	}

	@Override //Going to check next record is available or not using has_more attribute which i got in json through stackexchange api
	public boolean hasNext() {
		HttpResponse<JsonNode> response;
		boolean resultmore = true;
		try {
			response = Unirest.get("https://api.stackexchange.com/2.2/search")
					.header("accept", "application/json")
					.queryString("order", "desc")
					.queryString("sort", "activity")
					.queryString("intitle", searchQuery)
					.queryString("site", "stackoverflow")
					.queryString("page", Integer.toString(page))
					.queryString("pagesize", Integer.toString(pagesize))
					.queryString("key", "Py*Mz)a1NRJcefLgEefmgg((").asJson();
			if (response.getBody().getObject().get("has_more").toString() == "true") {
				resultmore = true;
			} else {
				resultmore = false;
			}

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			resultmore = false;
			e.printStackTrace();
		}
		return resultmore;
	}

	@Override //Implemented logic to get 25 records at a time then will get other new 25 records
	public HttpResponse<JsonNode> next() {
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get("https://api.stackexchange.com/2.2/search")
					.header("accept", "application/json")
					.queryString("order", "desc")
					.queryString("sort", "activity")
					.queryString("intitle", searchQuery)
					.queryString("site", "stackoverflow")
					.queryString("page", Integer.toString(page))
					.queryString("pagesize", Integer.toString(pagesize))
					.queryString("key", "Py*Mz)a1NRJcefLgEefmgg((").asJson();
			page = page + 25;
			pagesize = 25;
			
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

}
