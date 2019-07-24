package commands.general;

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
	public String getHelpText() { return "Will search derpibooru for the tags entered, if KittyBot's content filter allows it"; }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(guild.contentRating == KittyRating.Filtered)
		{
			
			try 
			{
				response = searcher.getDerpi(input.args + " safe").output();
				res.send(response);
			}
			catch(Exception e)
			{
				res.send(LocStrings.stub("DerpiError"));
			}
			
		}
		else
		{
			if(input.args == null || input.args.length() == 0)
				res.send("DerpiNoSearchError");
			else
			{
				response = searcher.getDerpi(input.args).output();
				try 
				{
					res.send(response);
				}
				catch(Exception e)
				{
					res.send(LocStrings.stub("DerpiError"));
				}
			}
		}
		
		if(input.args.startsWith("ID:"))
		{
			response = searcher.getDerpiByID(input.args.substring(input.args.indexOf(':'))).output();
			try 
			{
				res.send(response);
			}
			catch(Exception e)
			{
				res.send(LocStrings.stub("DerpiError"));
			}
		}
	}
}