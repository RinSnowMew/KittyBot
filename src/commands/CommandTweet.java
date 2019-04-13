package commands;

import core.Command;
import core.Localizer;
import dataStructures.*;
import network.NetworkTwitter;

public class CommandTweet extends Command 
{
	NetworkTwitter tweet = new NetworkTwitter();
	public CommandTweet(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("With an input of x,y,z where x y and z are all choices, kitty will choose one"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		try {
			res.Call(tweet.tweet(input.args));
		} catch (Exception e) {
			res.Call(Localizer.Stub("Tweet command failed!"));
		}
	}
}
