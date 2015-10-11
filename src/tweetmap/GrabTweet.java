package tweetmap;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import org.json.JSONObject;

public class GrabTweet {
	
	private DynamoManager DynamoMgr = new DynamoManager();
	
	void getTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("")
          .setOAuthConsumerSecret("")
          .setOAuthAccessToken("")
          .setOAuthAccessTokenSecret("");
        
       TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
       StatusListener listener = new StatusListener() {
           @Override
           public void onStatus(Status status) {
        	   if (status.getGeoLocation() == null)
        		   return;
        	   DynamoMgr.inertTweet(new TweetItem(status));
           }

           @Override
           public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
               //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
           }

           @Override
           public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
               //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
           }

           @Override
           public void onScrubGeo(long userId, long upToStatusId) {
               //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
           }

           @Override
           public void onStallWarning(StallWarning warning) {
               //System.out.println("Got stall warning:" + warning);
           }

           @Override
           public void onException(Exception ex) {
               ex.printStackTrace();
           }
       };
       twitterStream.addListener(listener);
       twitterStream.sample();
	}
	
	public static void main(String[] args) {
		GrabTweet tweetGraber = new GrabTweet();
		tweetGraber.getTwitter();
	}
}
