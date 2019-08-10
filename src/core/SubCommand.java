package core;

import dataStructures.*;
import utils.GlobalLog;
import utils.LogFilter;

public abstract class SubCommand 
{
	private KittyRole roleLevel;
	private KittyRating contentRating;
	
	
	public SubCommand(KittyRole roleLevel, KittyRating contentRating)
	{
		this.roleLevel = roleLevel;
		this.contentRating = contentRating;
	}
	
	private void Reject(KittyUser user, String reason)
	{
		GlobalLog.warn(LogFilter.Command, this.getClass().getSimpleName() + " from " + user.name + " rejected due to command's " + reason);
	}
	
	private boolean CanCall(KittyGuild guild, KittyChannel channel, KittyUser user)
	{	
		if(guild.contentRating.getValue() < contentRating.getValue())
		{
			Reject(user, "content rating");
			return false;
		}
		
		//TODO: ADD CHANNEL CHECK HERE
		
		if(user.getRole().getValue() >= roleLevel.getValue())
		{
			return true;
		}
		
		Reject(user, "permissions");
		return false;
	}
	
	// Called by the Command manager - this will run the command 
	// if the issuing user has the permission to do so!
	protected final SubCommandFormattable Invoke(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		if(!CanCall(guild, channel, user))
			return null;
		
		return OnRun(guild, channel, user, input);
	}
	
	public KittyRating Rating()
	{
		return contentRating;
	}
		
	public KittyRole RequiredRole()
	{
		return roleLevel;
	}
	
	public abstract SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input);
}