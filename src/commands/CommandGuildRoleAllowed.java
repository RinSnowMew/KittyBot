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

public class CommandGuildRoleAllowed extends Command
{
	public CommandGuildRoleAllowed(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("GuildRoleAllowedInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String [] roles = input.args.split(",");
		String role;
		
		for(int i = 0; i < roles.length; i++)
		{
			role = roles[i].trim().toLowerCase();
			if(guild.roleList.contains(role))
			{
				res.Call(LocStrings.Stub("GuildRoleAllowedDuplicate"));
			}
			else
			{
				guild.roleList.add(role);
				res.Call(String.format(LocStrings.Stub("GuildRoleAllowedSuccess"), role));
			}
		}
	}
}