package edu.csula.datascience.acquisition;

import java.util.Collection;
import java.util.List;

/**
 * Interface to define collector behavior
 *
 * It should be able to download data from source and save data.
 */
public interface Collector<T, R> {
    /**
     * Mungee method is to clean data. e.g. remove data rows with errors
     */
    // Collection<T> mungee(Collection<R> src);
    // void save(Collection<T> data);
	
    List<TweetModel> mungee(List<TweetModel> src);
	
    void save(TweetModel data);
    
    List<TweetModel> getAllTweets();
}
