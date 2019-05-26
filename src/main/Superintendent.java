package main;

import java.util.concurrent.atomic.AtomicInteger;

import core.DatabaseManager;
import core.LocCommands;
import core.LocStrings;
import core.RPManager;
import core.Stats;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import offline.Ref;

// Manages the fiddly bits and upkeep of the bot - while not 
public class Superintendent
{
	// This is run on the JDA GuildMessageReceivedEvent before anything else happens.
	public static boolean PreProcessSetup(GuildMessageReceivedEvent event, Stats stats)
	{
		// Verify we have an event
		if(event == null)
			return false;
		
		// Track number of messages seen
		stats.NoteMessageEvent();
		
		// If we're mid-shutdown, no more commands.
		if(stats.GetIsShuttingDown())
			return false;
		
		// Swat down highest ban level immediately before even hitting command parsing.
		for(int i = 0; i < Ref.alwaysIgnore.length; ++i)
		{
			String id = event.getAuthor().getId();
			if(id.equals(Ref.alwaysIgnore[i]))
				return false;
		}
		
		return true;
	}
	
	// This runs after all the objects have been constructed! 
	// Modifications to objects here stick!
	public static boolean PostProcessSetup(GuildMessageReceivedEvent event, KittyUser user, KittyGuild guild, KittyChannel channel, Response res, UserInput input)
	{
		// Verify we have everything. From here out, we promise we have that all.
		if(event == null || user == null || guild == null || channel == null || res == null || input == null)
			return false;
		
		// If the guild member is the owner, give them admin role by default.
		if(event.getMember().isOwner())
			user.ChangeRole(KittyRole.Admin);
		
		// Give devs the dev role
		for(int i = 0; i < Ref.devIDs.length; ++i)
		{
			if(event.getAuthor().getId().equals(Ref.devIDs[i]))
			{
				user.ChangeRole(KittyRole.Dev);
				break;
			}
		}
		
		return true;
	}
	
	// Only called once per command. Good for lazily updating.
	// Happens just before the command / plugin runs.
	public static boolean PerCommandUpkeepPre()
	{
		// Upkeep localization system's file monitoring
		LocStrings.Upkeep();
		LocCommands.Upkeep();
		
		return true;
	}
	
	private static final Integer delayTimerReset = 5;
	private static AtomicInteger delayTimerCurrent = new AtomicInteger(delayTimerReset);
	
	// This is for stuff that we need to do on a regular basis, but don't 
	// necessarily want running at all points in time. 
	// Happens just after the command / plugin runs.
	public static boolean PerCommandUpkeepPost(JDA bot, DatabaseManager databaseManager)
	{
		// Upkeep database lazily on occasion
		if(delayTimerCurrent.decrementAndGet() < 0)
		{
			databaseManager.Upkeep();
			delayTimerCurrent.set(delayTimerReset);
		}
		
		// Update command-specific
		RPManager.Upkeep(bot);
		
		return true;
	}
}
