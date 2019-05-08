package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;
import network.NetworkColiru;

public class CommandGuildRoleList extends Command
{
	NetworkColiru compiler = new NetworkColiru();
	
	public CommandGuildRoleList(KittyRole level, KittyRating rating) { super(level, rating);}

	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res) 
	{
		String roles = ""; 
		if(guild.roleList.isEmpty())
		{
			res.Call(String.format(LocStrings.Stub("GuildRoleListEmpty")));
			return;
		}
		for(int i = 0; i < guild.roleList.size(); i++)
		{ 
			if(i > 0)
				roles += " and "; 
			roles += guild.roleList.get(i);
		}
		res.Call(String.format(LocStrings.Stub("GuildRoleListOutput"), roles));
	}
}