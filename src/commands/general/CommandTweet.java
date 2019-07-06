package commands.general;

import core.Command;
import core.LocStrings;
import dataStructures.*;
import network.NetworkTwitter;

public class CommandTweet extends Command 
{
	NetworkTwitter tweet = new NetworkTwitter();
	public CommandTweet(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("TweetInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		//TODO: Figure out how to pass pictures correctly
		try {
			res.send(tweet.tweet(input.args));
		} catch (Exception e) {
			res.send(LocStrings.stub("TweetError"));
		}
	}
}
