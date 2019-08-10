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

public class SubCommandJoin extends SubCommand
{
	public SubCommandJoin(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		if(guild.joinRaffle(user))
		{
			return new SubCommandFormattable (LocStrings.stub("RaffleJoinSuccess"));
		}
		else
		{
			return new SubCommandFormattable (LocStrings.stub("RaffleJoinFailure"));
		}
	}
}
