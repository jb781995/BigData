package edu.csula.datascience.acquisition;

import java.util.Iterator;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

//implemented StackOverflowSourceInterface
public interface StackOverflowSourceInterface<T> extends
		Iterator<HttpResponse<JsonNode>> {

}
