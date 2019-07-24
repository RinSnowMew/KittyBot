package commands.guildrole;

import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;

public class SubCommandAdd extends SubCommand
{
	public SubCommandAdd(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		String role = input.split(" ")[0];
		if(guild.roleList.contains(role))
		{
			if(guild.control.addRole(user.discordID, role))
			{
				 return new SubCommandFormattable("ADDED  RIN YOU NEED TO FIX THIS"); 
			}
			else
			{
				return new SubCommandFormattable("FAILED RIN YOU NEED TO FIX THIS TOO");
			}
		}
		else
		{
			return new SubCommandFormattable("NOT ALLOWED FIX THIS ONE TOO SNEP FACE");
		}
	}
	
}