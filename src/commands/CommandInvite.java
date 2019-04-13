package commands;

import core.Command;
import core.Localizer;
import dataStructures.*;
import offline.Ref;

public class CommandInvite extends Command
{
	public CommandInvite(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Provies a direct invite link for KittyBot"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call("https://discordapp.com/oauth2/authorize?&client_id="+ Ref.CliID 
				+"&scope=bot&permissions=8");
	}
}