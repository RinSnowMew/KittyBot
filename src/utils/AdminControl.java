package utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.managers.GuildController;

public class AdminControl 
{
	private GuildController guildCon; 
	private Guild guild;
	
	public AdminControl(Guild guild)
	{
		this.guild = guild;
		guildCon = guild.getController();
	}
	
	public boolean addRole(String memberID, String roleName)
	{
		try
		{
			guildCon.addRolesToMember(guild.getMemberById(memberID), guild.getRolesByName(roleName, true)).complete();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
}
