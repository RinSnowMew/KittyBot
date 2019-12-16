package commands.music;

import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.UserInput;

public class SubCommandSkip extends SubCommand
{
	public SubCommandSkip(KittyRole level, KittyRating rating) { super(level, rating); }
	{
		
	}
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input) 
	{
		return new SubCommandFormattable(guild.audio.skipVideo(guild.audio.player));
	}
}
