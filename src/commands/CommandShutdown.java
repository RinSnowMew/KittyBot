package commands;

import core.Command;
import core.DatabaseManager;
import core.Localizer;
import core.Stats;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

// NOTE(wisp): This is a sort of special command.
public class CommandShutdown extends Command
{
	public CommandShutdown(KittyRole level, KittyRating rating) { super(level, rating); }

	@Override
	public String HelpText() { return Localizer.Stub("Stops kitty. `-s` or `safe` as an argument attempts to sync off the database before shutdown."); }
	
	// Called when the command is run!
	@Override 
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		// Flag the shutdown immediately.
		Stats.instance.IndicateShutdown();
		
		boolean isSafe = false;
		switch(input.args.toLowerCase().trim())
		{
			case "s":
			case "safe":
			case "-s":
			case "-safe":
			case "snafe":
				isSafe = true;
				break;
		}
		
		if(isSafe)
		{
			DatabaseManager.instance.Upkeep(); // Force upkeep, this works so long as on main thread.
			res.CallImmediate(Localizer.Stub("Forced shutdown, database synced before abandoning threads."));
			System.exit(0);
		}
		else
		{
			res.CallImmediate(Localizer.Stub("Forced immediate shutdown, threads abandoned without sync."));
			System.exit(0);
		}
	}
}