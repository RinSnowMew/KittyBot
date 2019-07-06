package commands.poll;

import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;

public class SubCommandEnd extends SubCommand
{

	public SubCommandEnd(KittyRole roleLevel, KittyRating contentRating) {super(roleLevel, contentRating);	}

	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input) 
	{
		return new SubCommandFormattable (guild.endPoll());
	}
	
}