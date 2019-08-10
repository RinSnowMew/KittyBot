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

public class SubCommandAllowed extends SubCommand
{
	public SubCommandAllowed(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		String [] roles = input.args.split(",");
		String role;
		String formatted = "";
		for(int i = 0; i < roles.length; i++)
		{
			role = roles[i].trim();
			if(guild.roleList.contains(role))
			{
				formatted += String.format(LocStrings.stub("GuildRoleAllowedDuplicate"), role);
			}
			else
			{
				guild.roleList.add(role);
				formatted += (String.format(LocStrings.stub("GuildRoleAllowedSuccess"), role));
			}
		}
		return new SubCommandFormattable(formatted);
	}
}