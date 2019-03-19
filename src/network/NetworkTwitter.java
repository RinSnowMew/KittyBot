package network;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import offline.Ref;

public class NetworkTwitter 
{
	ConfigurationBuilder cbuilder = new ConfigurationBuilder();
	
	
	public NetworkTwitter()
	{
		cbuilder.setDebugEnabled(true)
			.setOAuthConsumerKey(Ref.twitterAPIKey)
			.setOAuthConsumerSecret(Ref.twitterAPISecret)
			.setOAuthAccessToken(Ref.twitterAccessToken)
			.setOAuthAccessTokenSecret(Ref.twitterAccessTokenSecret);
		
		
	}
	
	public String tweet(String tweetText) throws Exception
	{
		TwitterFactory tf = new TwitterFactory(cbuilder.build());
		Twitter twitter = tf.getInstance();
		Status status = twitter.updateStatus(tweetText);
		return "Status set to: `" + status.getText() + "`!";
		
	}
}
