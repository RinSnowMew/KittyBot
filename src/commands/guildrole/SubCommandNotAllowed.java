package commands.guildrole;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.UserInput;

public class SubCommandNotAllowed extends SubCommand
{
	public SubCommandNotAllowed(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		String lowerInput = input.args.toLowerCase();
		String [] roles = lowerInput.split(",");
		String formatted = "";
		
		if(roles.length < 1)
		{
			return new SubCommandFormattable(String.format(LocStrings.stub("GuildRoleNotAllowedNoArgs")));
		}
		
		for(String role:roles)
		{
			if(guild.roleList.contains(role.trim()))
			{
				guild.roleList.remove(role);
				formatted += String.format(LocStrings.stub("GuildRoleNotAllowedSuccess"), role);
			}
			else
			{
				formatted += String.format(LocStrings.stub("GuildRoleNotAllowedFailure"), role);
			}
		}
		return new SubCommandFormattable(formatted);
	}
}