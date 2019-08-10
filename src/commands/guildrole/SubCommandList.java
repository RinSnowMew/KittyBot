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

public class SubCommandList extends SubCommand
{
	
	public SubCommandList(KittyRole level, KittyRating rating) { super(level, rating);}

	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		String roles = ""; 
		if(guild.roleList.size() < 2)
		{
			return new SubCommandFormattable(String.format(LocStrings.stub("GuildRoleListEmpty")));
		}
		for(int i = 0; i < guild.roleList.size(); i++)
		{ 
			if(i > 0)
				roles += "\n"; 
			roles += guild.roleList.get(i);
		}
		return new SubCommandFormattable(String.format(LocStrings.stub("GuildRoleListOutput"), roles));
	}
}