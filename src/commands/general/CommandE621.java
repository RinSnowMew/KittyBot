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
		if(guild.contentRating == KittyRating.Filtered || !guild.channels.get(Long.getLong(channel.uniqueID)).mature)
		{
			if(!guild.channels.get(Long.getLong(channel.uniqueID)).mature)
			{
				res.send("This channel is SFW only! Have some (probably) wholesome content ^^~");
			}
			response = searcher.getE621(input.args + " rating:safe").output();
			try 
			{
				res.send(response);
			}
			catch(Exception e)
			{
				res.send(LocStrings.stub("E621Blacklisted"));
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
					res.send(LocStrings.stub("E621Blacklisted"));
				}
			}
		}
	}
}