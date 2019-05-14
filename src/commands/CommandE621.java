package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;
import network.NetworkE621;

public class CommandE621 extends Command
{
	NetworkE621 searcher = new NetworkE621();
	KittyEmbed response;
	
	public CommandE621(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return "Will search e621 for the tags entered, if KittyBot's content filter allows it"; }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		
		if(guild.contentRating == KittyRating.Filtered)
		{
			response = searcher.getE621(input.args + " rating:safe").output();
			try 
			{
				res.CallEmbed(response);
			}
			catch(Exception e)
			{
				res.Call(LocStrings.Stub("E621Error"));
			}
		}
		else
		{
			if(input.args == null || input.args.length() == 0)
				res.Call("E621NoSearchError");
			else
			{
				try 
				{
					response = searcher.getE621(input.args).output();
					res.CallEmbed(response);
				}
				catch(Exception e)
				{
					res.Call(LocStrings.Stub("E621Error"));
				}
			}
		}
	}
}