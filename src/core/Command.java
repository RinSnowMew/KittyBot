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
	
	private void reject(KittyUser user, String reason)
	{
		GlobalLog.warn(LogFilter.Command, this.getClass().getSimpleName() + " from " + user.name + " rejected due to command's " + reason);
	}
	
	// Determine if we're exclusive enough for this command and 
	// if the command is permitted by the guild we're in
	private boolean canCall(KittyGuild guild, KittyChannel channel, KittyUser user)
	{	
		if(guild.contentRating.getValue() < contentRating.getValue())
		{
			reject(user, "content rating");
			return false;
		}
		
		//TODO: ADD CHANNEL CHECK HERE
		
		if(user.getRole().getValue() >= roleLevel.getValue())
		{
			return true;
		}
		
		reject(user, "permissions");
		return false;
	}
	
	// Called by the Command manager - this will run the command 
	// if the issuing user has the permission to do so!
	protected final void invoke(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(!canCall(guild, channel, user))
			return;
		
		onRun(guild, channel, user, input, res);
	}
	
	public ArrayList<String> registeredNames()
	{
		return registeredNames;
	}
	
	public KittyRating rating()
	{
		return contentRating;
	}
	
	public KittyRole requiredRole()
	{
		return roleLevel;
	}
	
	// OVERRIDE ME! (This is not required but advised!)
	// Returns if the command succeeded or not.
	public String getHelpText() 
	{
		return "No help text has been added yet for " + this.getClass().getSimpleName() + "!";
	}
	
	// OVERRIDE ME! (This is required)
	// Returns if the command succeeded or not.
	public abstract void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res);
}
