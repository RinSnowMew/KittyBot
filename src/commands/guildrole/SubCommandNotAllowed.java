package commands.guildrole;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;

public class SubCommandNotAllowed extends SubCommand
{
	public SubCommandNotAllowed(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		input = input.toLowerCase();
		String [] roles = input.split(",");
		for(String role:roles)
		{
			if(guild.roleList.contains(role.trim()))
			{
				guild.roleList.remove(role);
				return new SubCommandFormattable(LocStrings.stub("GuildRoleNotAllowedSuccess"));
			}
			else
			{
				return new SubCommandFormattable(String.format(LocStrings.stub("GuildRoleNotAllowedFailure"), role));
			}
		}
		return new SubCommandFormattable(String.format(LocStrings.stub("GuildRoleNotAllowedNoArgs")));
	}
}