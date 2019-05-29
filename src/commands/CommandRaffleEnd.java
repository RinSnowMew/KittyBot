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

public class CommandRaffleEnd extends Command
{
	public CommandRaffleEnd(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("RaffleEndInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(guild.endRaffle())
		{
			res.Call(LocStrings.Stub("RaffleEndSuccess"));
		}
		else
		{
			res.Call(LocStrings.Stub("RaffleEndFailure"));
		}
	}
}