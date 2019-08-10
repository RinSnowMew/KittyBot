package commands.mod;

import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.UserInput;

public class SubCommandKick extends SubCommand
{
	public SubCommandKick(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		if(guild.control.kickMember(input.mentions[0].discordID))
		{
			return new SubCommandFormattable("Success");
		}
		return new SubCommandFormattable("Fail");
	}
}