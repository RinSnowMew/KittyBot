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

public class CommandRaffleSpin extends Command
{
	public CommandRaffleSpin(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("RaffleSpinInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		try
		{
			res.Call(String.format(LocStrings.Stub("RaffleSpinSuccess"), guild.chooseRaffleWinner().name));
		}
		catch(Exception e)
		{
			res.Call(LocStrings.Stub("RaffleSpinFailure"));
		}
	}
}