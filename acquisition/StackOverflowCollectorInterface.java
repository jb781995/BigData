package edu.csula.datascience.acquisition;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

//implemented stackoverflow interface
public interface StackOverflowCollectorInterface<T, R> {

	HttpResponse<JsonNode> mungee(HttpResponse<JsonNode> src);

	void save(HttpResponse<JsonNode> data);

}
