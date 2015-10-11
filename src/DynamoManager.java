import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryFilter;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.RangeKeyCondition;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;


public class DynamoManager {

	static final String tableName = "tweet";
	private DynamoDB dynamoDB = new DynamoDB ((AmazonDynamoDB) new 
       		AmazonDynamoDBClient(new ProfileCredentialsProvider("YuzheShenCloud")));
	private Table table = dynamoDB.getTable(tableName);
	
	// for testing
	ItemCollection<QueryOutcome> getPoints(String bf, String af) {
		RangeKeyCondition rangeKeyCondition =
				new RangeKeyCondition("time")
					.gt(bf)
					.lt(af); //2015-10-11 10:18:14 2015-10-10 00:00:00
	    QueryFilter filter = new QueryFilter("");
	    filter.eq("a");
	    QuerySpec spec = new QuerySpec()
	        .withHashKey("keyword", "none")
	        .withRangeKeyCondition(rangeKeyCondition)
	        .withQueryFilters(filter);
	    ItemCollection<QueryOutcome> items = table.query(spec);
	    return items;
	}
	
	
	public void inertTweet(TweetItem tweet) {
		Item dynamoItem = new Item();
		if (tweet.keywords.isEmpty()) {
			dynamoItem
			.withPrimaryKey("keyword", "none")
			.withLong("twid", tweet.twid)
			.withString("time", tweet.timeStamp)
			.withDouble("lon", tweet.lon)
			.withDouble("lat", tweet.lat);
		}
		table.putItem(dynamoItem);
	}
}
