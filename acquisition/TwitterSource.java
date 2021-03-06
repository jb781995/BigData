package edu.csula.datascience.acquisition;

import com.google.common.collect.Lists;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collection;
import java.util.List;

/**
 * An example of Source implementation using Twitter4j api to grab tweets
 */
public class TwitterSource implements Source<Status> {
	
    private long minId;
    private final String searchQuery;

    public TwitterSource(long minId, String query) {
        this.minId = minId;
        this.searchQuery = query;
    }

    @Override
    public boolean hasNext() {
        return minId > 0;
    }

    @Override
    public Collection<Status> next() {
        List<Status> list = Lists.newArrayList();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey(("H0MB0igw64DmhPSEBLgRyuHbp"))
        .setOAuthConsumerSecret(("oTUzVLLK1iJPCd95QJHI358s3zLlRzXaXhZf4gTxV0Fwq2nH8W"))
        .setOAuthAccessToken(("2889696923-qqMTF0enWj46GQASHZMpLmZyHTpqz8XkBlHw0Ck"))
        .setOAuthAccessTokenSecret(("Ph5DztHQ7KgT9CokBZVuAolmJxRuPKg5C6ZMfkcF6D3aa"));
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        Query query = new Query(searchQuery);
        query.setLang("EN");
        query.setSince("20140101");
        if (minId != Long.MAX_VALUE) {
            query.setMaxId(minId-1);
        }

        list.addAll(getTweets(twitter, query));

        return list;
    }

    private List<Status> getTweets(Twitter twitter, Query query) {
    	int pageno = 1;
        QueryResult result;
        List<Status> list = Lists.newArrayList();
        try {
            do {
                result = twitter.search(query);
                
                List<Status> tweets = result.getTweets();
                
                System.out.println("# Tweets:\t" + tweets.size());
                
                for (Status tweet : tweets) {
                    minId = Math.min(minId, tweet.getId());
                }
                list.addAll(tweets);
            } while ((query = result.nextQuery()) != null);
        } catch (TwitterException e) {
            // Catch exception to handle rate limit and retry
            e.printStackTrace();
            System.out.println("Got twitter exception. Current min id " + minId);
            try {
                Thread.sleep(e.getRateLimitStatus()
                    .getSecondsUntilReset() * 1000);
                list.addAll(getTweets(twitter, query));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

        return list;
    }
}
