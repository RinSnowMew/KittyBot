package core;

import java.util.ArrayList;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.GlobalLog;
import utils.LogFilter;

// One note about string[] ... We can't change whitelist 
// on the command without re-registering it.
public abstract class Command 
{
	public ArrayList<String> registeredNames;
	private KittyRole roleLevel;
	private KittyRating contentRating;
	
	public Command(KittyRole roleLevel, KittyRating contentRating)
	{
		this.registeredNames = new ArrayList<String>();
		this.roleLevel = roleLevel;
		this.contentRating = contentRating;
	}
	
	private void Reject(KittyUser user, String reason)
	{
		GlobalLog.Warn(LogFilter.Command, this.getClass().getSimpleName() + " from " + user.name + " rejected due to command's " + reason);
	}
	
	// Determine if we're exclusive enough for this command and 
	// if the command is permitted by the guild we're in
	private boolean CanCall(KittyGuild guild, KittyChannel channel, KittyUser user)
	{	
		if(guild.contentRating.getValue() < contentRating.getValue())
		{
			Reject(user, "content rating");
			return false;
		}
		
		//TODO: ADD CHANNEL CHECK HERE
		
		if(user.GetRole().getValue() >= roleLevel.getValue())
		{
			return true;
		}
		
		Reject(user, "permissions");
		return false;
	}
	
	// Called by the Command manager - this will run the command 
	// if the issuing user has the permission to do so!
	protected final void Invoke(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(!CanCall(guild, channel, user))
			return;
		
		OnRun(guild, channel, user, input, res);
	}
	
	public ArrayList<String> RegisteredNames()
	{
		return registeredNames;
	}
	
	public KittyRating Rating()
	{
		return contentRating;
	}
	
	public KittyRole RequiredRole()
	{
		return roleLevel;
	}
	
	// OVERRIDE ME! (This is not required but advised!)
	// Returns if the command succeeded or not.
	public String HelpText() 
	{
		return "No help text has been added yet for " + this.getClass().getSimpleName() + "!";
	}
	
	// OVERRIDE ME! (This is required)
	// Returns if the command succeeded or not.
	public abstract void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res);
}
