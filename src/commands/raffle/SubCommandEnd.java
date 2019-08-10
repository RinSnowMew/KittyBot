package commands.raffle;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.UserInput;

public class SubCommandEnd extends SubCommand
{
	public SubCommandEnd(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		if(guild.endRaffle())
		{
			return new SubCommandFormattable (LocStrings.stub("RaffleEndSuccess"));
		}
		else
		{
			return new SubCommandFormattable (LocStrings.stub("RaffleEndFailure"));
		}
	}
}