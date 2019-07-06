package commands.raffle;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.*;

public class SubCommandStart extends SubCommand
{
	public SubCommandStart(KittyRole level, KittyRating rating) { super(level, rating); }
	
	public int beanCost;
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		try
		{
			beanCost = Integer.parseInt(input);
		}
		catch(Exception e)
		{
			beanCost = 100; 
		}
		
		if(guild.startRaffle(beanCost))
		{
			return new SubCommandFormattable (String.format(LocStrings.stub("RaffleStartSuccess"), beanCost));
		}
		else
		{
			return new SubCommandFormattable (LocStrings.stub("RaffleStartFailure"));
		}
	}
}
