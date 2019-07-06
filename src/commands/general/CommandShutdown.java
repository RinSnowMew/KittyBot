package commands.general;

import core.Command;
import core.DatabaseManager;
import core.LocStrings;
import core.Stats;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.GlobalLog;
import utils.LogFilter;

// NOTE(wisp): This is a sort of special command.
public class CommandShutdown extends Command
{
	public CommandShutdown(KittyRole level, KittyRating rating) { super(level, rating); }

	@Override
	public String getHelpText() { return LocStrings.stub("ShutdownInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		// Flag the shutdown immediately.
		Stats.instance.indicateShutdown();
		
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
			// Force upkeep, this works so long as upkeep is on the main thread.
			res.sendImmediate(LocStrings.stub("ShutdownSafe"));
			DatabaseManager.instance.upkeep(); 
			GlobalLog.warn(LogFilter.Command, LocStrings.lookup("ShutdownSafe"));
			
			System.exit(0);
		}
		else
		{
			res.sendImmediate(LocStrings.stub("ShutdownUnsafe"));// "``");
			GlobalLog.warn(LogFilter.Command, LocStrings.lookup("ShutdownSafe"));
			System.exit(0);
		}
	}
}