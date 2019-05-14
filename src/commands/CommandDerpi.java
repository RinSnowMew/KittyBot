package commands;

import core.Command;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyEmbed;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import network.NetworkDerpi;

public class CommandDerpi extends Command
{
	NetworkDerpi searcher = new NetworkDerpi(); 
	KittyEmbed response;
	
	// Required constructor
	public CommandDerpi(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return "Will search derpibooru for the tags entered, if KittyBot's content filter allows it"; }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(guild.contentRating == KittyRating.Filtered)
		{
			
			try 
			{
				response = searcher.getDerpi(input.args + " safe").output();
				res.CallEmbed(response);
			}
			catch(Exception e)
			{
				res.Call(LocStrings.Stub("DerpiError"));
			}
			
		}
		else
		{
			if(input.args == null || input.args.length() == 0)
				res.Call("DerpiNoSearchError");
			else
			{
				response = searcher.getDerpi(input.args).output();
				try 
				{
					res.CallEmbed(response);
				}
				catch(Exception e)
				{
					res.Call(LocStrings.Stub("DerpiError"));
				}
			}
		}
	}
}