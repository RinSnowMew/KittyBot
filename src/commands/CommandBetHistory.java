package commands;

import core.Command;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandBetHistory extends Command
{
	public CommandBetHistory(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("BetHistoryInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call(String.format(LocStrings.Stub("BetHistoryOutput"), guild.beans.Get()));
	}
}