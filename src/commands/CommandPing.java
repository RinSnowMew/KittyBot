package commands;

import core.*;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

// A ping! Good example command.
public class CommandPing extends Command
{
	public CommandPing(KittyRole level, KittyRating rating) { super(level, rating); }

	@Override
	public String HelpText() { return Localizer.Stub("Will respond with Pong!"); }
	
	// Called when the command is run!
	@Override 
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call(Localizer.Stub("Pong!"));
	}
}
