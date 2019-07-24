package commands.general;

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
	public String getHelpText() { return "LOUDSCREEHING"; }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.sendToChannel("AHHH", channel.uniqueID);
	}
}
