package commands.guildrole;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.*;

public class SubCommandRemove extends SubCommand
{
	public SubCommandRemove(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		String role = input.split(" ")[0];
		String formatted = "";
		if(guild.roleList.contains(role))
		{
			if(guild.control.removeRole(user.discordID, role))
			{
				formatted += (String.format(LocStrings.stub("GuildRoleRemoveSuccess"), role, user.name));
			}
			else
			{
				formatted += (String.format(LocStrings.stub("GuildRoleRemoveFailure"), role, user.name));
			}
		}
		else
		{
			formatted += (String.format(LocStrings.stub("GuildRoleRemoveNotAllowed"), role));
		}
		
		return new SubCommandFormattable(formatted);
	}
}