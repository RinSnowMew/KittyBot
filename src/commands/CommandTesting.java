package commands;

import core.Command;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandTesting extends Command
{

	public CommandTesting(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return "LOUDSCREEHING"; }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.CallToChannel("AHHH", channel.uniqueID);
	}
}
