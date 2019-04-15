package commands;

import core.Command;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandAddGuildRole extends Command
{
	public CommandAddGuildRole(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("AddGuildRoleInfo"); };
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String role = input.args.split(" ")[0];
		if(guild.allowedRole.contains(role))
		{
			if(guild.control.addRole(user.discordID, role))
			{
				res.Call(String.format(LocStrings.Stub("AddGuildRoleSuccess"), role, user.name));
			}
			else
			{
				res.Call(String.format(LocStrings.Stub("AddGuildRoleFailure"), role, user.name));
			}
		}
		else
		{
			res.Call(String.format(LocStrings.Stub("AddGuildRoleNotAllowed"), role));
		}
	}
}