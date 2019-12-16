package commands.music;

import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.UserInput;

public class SubCommandStop extends SubCommand
{

	public SubCommandStop(KittyRole roleLevel, KittyRating contentRating) {super(roleLevel, contentRating);}

	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input) 
	{
		
		return new SubCommandFormattable(guild.audio.stopPlayer(guild.audio.player));
	}
	
}
