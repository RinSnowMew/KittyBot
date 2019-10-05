package commands.general;

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
	public String getHelpText() { return "Will search e621 for the tags entered, if KittyBot's content filter allows it"; }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String userMessage = null;
		input.args = input.args.trim();
		if(input.args.startsWith("md5:") && input.args.contains(" "))
		{
			userMessage = input.args.substring(input.args.indexOf(" "));
			input.args = input.args.substring(0, input.args.indexOf("."));
		}
		
		System.out.println("LOOKING FOR: " + input.args);
		if(guild.contentRating == KittyRating.Filtered)
		{
			response = searcher.getE621(input.args + " rating:safe").output();
			try 
			{
				res.send(response);
			}
			catch(Exception e)
			{
				res.send(LocStrings.stub("E621Error"));
			}
		}
		else
		{
			if(input.args == null || input.args.length() == 0)
				res.send("E621NoSearchError");
			else
			{
				try 
				{
					response = searcher.getE621(input.args).output();
					res.send(response);
				}
				catch(Exception e)
				{
					res.send(LocStrings.stub("E621Error"));
				}
			}
		}
	}
}