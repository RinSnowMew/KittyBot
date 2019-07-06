package commands.general;

import core.Command;
import core.LocStrings;
import dataStructures.*;
import offline.Ref;

public class CommandInvite extends Command
{
	public CommandInvite(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("InviteInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.send("https://discordapp.com/oauth2/authorize?&client_id="+ Ref.CliID 
				+"&scope=bot&permissions=8");
	}
}