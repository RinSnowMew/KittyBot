package commands.guildrole;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.*;

public class SubCommandRemove extends SubCommand
{
	public SubCommandRemove(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		String [] roles = input.args.split(",");
		String role;
		String formatted = "";
		for(int i = 0; i < roles.length; i ++)
		{
			role = roles[i].trim().toLowerCase();
			if(guild.roleList.contains(role))
			{
				if(guild.control.removeRole(user.discordID, role))
				{
					formatted += (String.format(LocStrings.stub("GuildRoleRemoveSuccess"), role, user.name)) + "\n";
				}
				else
				{
					formatted += (String.format(LocStrings.stub("GuildRoleRemoveFailure"), role, user.name)) + "\n";
				}
			}
			else
			{
				formatted += (String.format(LocStrings.stub("GuildRoleRemoveNotAllowed"), role)) + "\n";
			}
		}
		
		
		return new SubCommandFormattable(formatted);
	}
}