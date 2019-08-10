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
	
	public boolean removeRole(String memberID, String roleName)
	{
		try
		{
			guildCon.removeRolesFromMember(guild.getMemberById(memberID), guild.getRolesByName(roleName, true)).complete();
		}
		catch(Exception e)
		{
			return false; 
		}
		return true;
	}
	
	public boolean kickMember(String memberID)
	{
		try
		{
			guildCon.kick(guild.getMemberById(memberID)).complete();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public boolean muteMember(String memberID)
	{
		try
		{
			guildCon.setMute(guild.getMemberById(memberID), true);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean banMember(String memberID)
	{
		try
		{
			guildCon.ban(guild.getMemberById(memberID), 0).complete();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
