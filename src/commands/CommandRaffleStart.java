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

public class CommandRaffleStart extends Command
{
	public CommandRaffleStart(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("RaffleStartInfo"); }
	
	public int beanCost;
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		try
		{
			beanCost = Integer.parseInt(input.args);
		}
		catch(Exception e)
		{
			beanCost = 100; 
		}
		if(guild.startRaffle(beanCost))
		{
			res.Call(String.format(LocStrings.Stub("RaffleStartSuccess"), beanCost));
		}
		else
		{
			res.Call(LocStrings.Stub("RaffleStartFailure"));
		}
	}
}
