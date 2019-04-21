package main;

import javax.security.auth.login.LoginException;
import core.*;
import core.lua.PluginManager;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import offline.*;
import utils.GlobalLog;
import net.dv8tion.jda.core.*;

// http://www.slf4j.org/ - this JDA logging tool has been disabled by specifying NOP implementation.
// This is the application entry point, and bot startup location!

@SuppressWarnings("unused")
public class Main extends ListenerAdapter
{
	// Variables and bot specific objects
	private static JDA kitty;
	private static DatabaseManager databaseManager; 
	private static CommandEnabler commandEnabler;
	private static CommandManager commandManager;
	private static Stats stats;
	private static RPManager rpManager;
	private static PluginManager pluginManager;
	
	// Main test location
	public static void main(String[] args) throws InterruptedException, LoginException, Exception
	{
		// Factory startup. The ordering is intentional.
		databaseManager = ObjectBuilderFactory.ConstructDatabaseManager();
		commandEnabler = ObjectBuilderFactory.ConstructCommandEnabler();
		commandManager = ObjectBuilderFactory.ConstructCommandManager(commandEnabler);
		stats = ObjectBuilderFactory.ConstructStats(commandManager);
		rpManager = ObjectBuilderFactory.ConstructRPManager();
		pluginManager = ObjectBuilderFactory.ConstructPluginManager();
		
		// Bot startup
		kitty = new JDABuilder(AccountType.BOT).setToken(Ref.TestToken).buildBlocking();
		kitty.getPresence().setGame(Game.playing("with digital yarn"));
		kitty.addEventListener(new Main());
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{	
		// Tweak the event as necessary
		if(!PreProcessSetup(event))
			return;
		
		// Factory objects
		KittyUser user = ObjectBuilderFactory.ExtractUser(event);
		KittyGuild guild = ObjectBuilderFactory.ExtractGuild(event);
		KittyChannel channel = ObjectBuilderFactory.ExtractChannel(event);
		
		// Specialized uncached objects
		Response response = new Response(event, kitty);
		UserInput input = new UserInput(event, guild);
		
		// Tweak object construction as necessary
		if(!PostProcessSetup(event, user, guild, channel, response, input))
			return;
				
		// Track beans!
		user.ChangeBeans(1);		
		
		// RP logging system
		RPManager.instance.addLine(channel, user, input);
		
		// Run plugins right before invoking the commands but after all other setup
		String output = pluginManager.CallAll(input.message);
		
		// Spin up the command if no plugins ran. Otherwise, send a response.
		if(output == null)
			commandManager.InvokeOnNewThread(guild, channel, user, input, response);
		else
			response.Call(output);
		
		// Run any upkeep we need to
		PerCommandUpkeep();
	}

	// This is run on the JDA GuildMessageReceivedEvent before anything else happens.
	public static boolean PreProcessSetup(GuildMessageReceivedEvent event)
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
	
	// This is for stuff that we need to do on a regular basis, but don't 
	// necessarily want running at all points in time.
	private static boolean PerCommandUpkeep()
	{
		databaseManager.Upkeep();
		RPManager.Upkeep(kitty);
		
		return true;
	}
}