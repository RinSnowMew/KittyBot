package core;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import commands.*;
import dataStructures.*;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import utils.GlobalLog;
import utils.LogFilter;

// NOTE(wisp): Isolated factory to assist with storage and caching if needed.
// This also minimizes the number of places JDA interacts with our codebase.
// As it stands, if the object name begins with Kitty, it's constructed here.
// TODO: Make all methods ID based instead of event based 
public class ObjectBuilderFactory 
{
	// Key: guild string id, Value: guild information
	private static HashMap<String, KittyGuild> guildCache;

	// Key: channel string id, Value: channel information
	private static HashMap<String, KittyChannel> channelCache;
	
	// Key: guild string id + user string id, Value: user information
	private static HashMap<String, KittyUser> userCache;
	
	// For tracking and managing object sync
	private static DatabaseManager database;
	
	// Stats tracking and whatnot... for setting stats too internally potentially
	private static Stats stats;
	
	// RPManger for tracking RP system
	private static RPManager rpManager; 
	
	// Localization classes - these are singletons, but should be initialized before almost all other 
	// things so their inclusion in the factory is to ensure they're started at the correct time.
	@SuppressWarnings("unused") private static LocStrings locStrings;
	@SuppressWarnings("unused") private static LocCommands locCommands;
	
	// Handles if we can or can't use specific commands, parsing a config file based on loc data to do so.
	private static CommandEnabler commandEnabler;
						
	// Lazy initialization multithreaded mutex stuff to prevent explosions.
	// TODO: Investigate using 'synchronized' instead potentially
	private static boolean hasInitialized;
	private static Semaphore initMutex = new Semaphore(1);
	
	// This is it, this is how the lazy init starts!
	private static void LazyInit()
	{
		if(hasInitialized)
			return;
		
		try
		{
			initMutex.acquire();
			try
			{
				// structure initialization 
				// Construct necessary data structures.
				guildCache = new HashMap<String, KittyGuild>();
				userCache = new HashMap<String, KittyUser>();
				channelCache = new HashMap<String, KittyChannel>();
				database = null;
				stats = null;
				
				// Start by reading from things that are external. Because
				// we require these things to be resolved before the rest of the application,
				// we place them here.
				locStrings = new LocStrings();
				locCommands = new LocCommands();
			}
			finally
			{
				initMutex.release();
				hasInitialized = true;
			}
		}
		catch(InterruptedException ie)
		{
			GlobalLog.Error(LogFilter.Core, "Issue during object builder lazy initialization."
				+ " The factory was not initialized, and kitty will not be able to continue functionally.");
		}
	}
	
	// Explicitly locks: guildCache
	public static KittyGuild ExtractGuild(GuildMessageReceivedEvent event)
	{
		LazyInit();
		
		// Look up the guild. This process can only happen in a single-threaded way
		// because of the nature of the cache. We wait until the last second to 
		// look up the guild.
		String uid = event.getGuild().getId();
		
		// ince we're lazily initialized, we can synchronize w/ the 
		// guildCache object now instead of having to use a mutex.
		KittyGuild guild = null;
		synchronized (guildCache)
		{
			KittyGuild cachedGuild = guildCache.get(uid);
			if(cachedGuild != null)
			{
				guild = cachedGuild;
			}
			else
			{
				// Construct a new guild with defaults
				guild = new KittyGuild(uid);
				DatabaseManager.instance.Register(guild);
				guildCache.put(uid, guild);
			}
		}

		return guild;
	}
	
	// Explicitly locks: guildCache
	public static KittyRole ExtractRole(GuildMessageReceivedEvent event)
	{
		LazyInit();

		// Looks up the user role. If none is found we check to see if they own
		// the guild if not, they're assumed to be
		// allowed to use the bot at a general level.
		KittyRole role = KittyRole.General;
		
		if(event.getAuthor().getId() == event.getGuild().getOwner().getUser().getId())
		{
			role = KittyRole.Admin;
		}
		
		String uid = event.getGuild().getId() + event.getAuthor().getId();
		synchronized(userCache)
		{
			KittyUser cachedUser = userCache.get(uid);
			if(cachedUser != null)
				role = cachedUser.GetRole();
		}
		
		return role;
	}
	
	// Explicitly locks: guildCache
	// Extracts the content rating information it can from the provided event.
	public static KittyRating ExtractContentRating(GuildMessageReceivedEvent event)
	{
		LazyInit();

		// Look up content rating of the guild, returns a safe content rating.
		KittyRating contentRating = KittyRating.Safe;
		String uid = event.getGuild().getId();
		synchronized(guildCache)
		{
			KittyGuild cachedGuild = guildCache.get(uid);
			if(cachedGuild != null)
				contentRating = cachedGuild.contentRating;
		}
		
		return contentRating;
	}
	
	// Implicitly locks guild cache by calling ExtractGuild
	public static KittyChannel ExtractChannel(GuildMessageReceivedEvent event)
	{
		LazyInit();
		
		String channelID = event.getChannel().getId();
		String guildID = event.getGuild().getId();
		KittyChannel channel = null;
		
		synchronized(channelCache)
		{
			KittyChannel cachedChannel = channelCache.get(channelID);
			
			if(cachedChannel != null)
			{
				channel = cachedChannel;
			}
			else
			{
				KittyGuild cachedGuild = guildCache.get(guildID);
				channel = new KittyChannel(channelID, cachedGuild);
				channelCache.put(channelID, channel);
			}
		}
		
		return channel;
	}
	
	// Implicitly locks guild cache by calling ExtractRole and ExtractGuild
	public static KittyUser ExtractUser(GuildMessageReceivedEvent event)
	{
		LazyInit();
		
		String uid = event.getGuild().getId() + event.getAuthor().getId();
		KittyUser user = null;
		synchronized(userCache)
		{			
			KittyUser cachedUser = userCache.get(uid);
			if(cachedUser != null)
			{
				updateUser(cachedUser, event.getMember());
				user = cachedUser;
			}
			else
			{
				KittyRole role = ExtractRole(event);
				KittyGuild guild = ExtractGuild(event);
				
				String name;
				if(event.getMember().getNickname() == null)
					name = event.getAuthor().getName();
				else
					name = event.getMember().getNickname();
				
				String discordID = event.getMember().getUser().getId(); 
				String avatarID = event.getAuthor().getAvatarUrl();
				user = new KittyUser(name, guild, role, uid, avatarID, discordID);
				DatabaseManager.instance.Register(user);
				userCache.put(uid, user);
			}
		}
		
		if(event.getMessage().getMentionedMembers().isEmpty())
			return user; 
		
		Member mentioned;
		for(int i = 0; i < event.getMessage().getMentionedMembers().size(); i++)
		{
			mentioned = event.getMessage().getMentionedMembers().get(i);
			if(mentioned.getNickname() != null)
				ExtractUserByJDAUser(event.getGuild().getId(), mentioned.getNickname(), 
					mentioned.getUser().getId(), mentioned.getUser().getAvatarUrl(), mentioned.getUser().getId());
			else
				ExtractUserByJDAUser(event.getGuild().getId(), mentioned.getUser().getName(), 
						mentioned.getUser().getId(), mentioned.getUser().getAvatarUrl(), mentioned.getUser().getId());
		}
		
		return user;
	}
	
	public static KittyUser ExtractUserByJDAUser(String guildID, String name, String userID, String avatarID, String discordID)
	{
		LazyInit();
		
		String uid = guildID + userID;
		KittyUser user = null;
		synchronized(userCache)
		{
			KittyUser cachedUser = userCache.get(uid);
			if(cachedUser != null)
			{
				if(name != null)
					cachedUser.name = name;
				cachedUser.avatarID = avatarID;
				user = cachedUser;
			}
			else
			{
				KittyRole role = KittyRole.General;
				KittyGuild guild = guildCache.get(guildID);
				user = new KittyUser(name, guild, role, uid, avatarID, discordID);
				DatabaseManager.instance.Register(user);
				userCache.put(uid, user);
			}
		}
		
		return user; 
	}
	
	public static KittyUser getCachedUser(String guildID, String userID)
	{
		String uid = guildID + userID;
		KittyUser user = null;
		synchronized(userCache)
		{
			user = userCache.get(uid);
		}
		return user; 
	}
	
	public static void updateUser(KittyUser user,  Member member)
	{
		if(member.getNickname() == null)
			user.name = member.getUser().getName();
		else
			user.name = member.getNickname();
		
		user.avatarID = member.getUser().getAvatarUrl();
	}
	
	// Constructs a CommandEnabler if it doesn't exist, and gets the existing one if it does. 
	public static CommandEnabler ConstructCommandEnabler()
	{
		LazyInit();

		if(commandEnabler == null)
			commandEnabler = new CommandEnabler();
		
		return commandEnabler;
	}
	
	// Default construction of the command manager. In order to remotely resolve command enabling
	// and disabling, what we do is construct the commands with a localized pair that is checked against
	// the CommandEnabler object passed in. In theory, we could have multiple CommandManagers, tho we can
	// only have one CommandEnabler.
	public static CommandManager ConstructCommandManager(CommandEnabler commandEnabler)
	{
		LazyInit();
		
		CommandManager manager = new CommandManager(commandEnabler);
		
		manager.Register(LocCommands.Stub("work"), new CommandDoWork(KittyRole.Dev, KittyRating.Safe));
		manager.Register(LocCommands.Stub("shutdown"), new CommandShutdown(KittyRole.Dev, KittyRating.Safe));
		manager.Register(LocCommands.Stub("stats"), new CommandStats(KittyRole.Dev, KittyRating.Safe));
		manager.Register(LocCommands.Stub("invite"), new CommandInvite(KittyRole.Dev, KittyRating.Safe));
		manager.Register(LocCommands.Stub("buildHelp"), new CommandHelpBuilder(KittyRole.Dev, KittyRating.Safe));
		manager.Register(LocCommands.Stub("tweet"), new CommandTweet(KittyRole.Dev, KittyRating.Safe));
		
		manager.Register(LocCommands.Stub("rating"), new CommandRating(KittyRole.Admin, KittyRating.Safe));
		manager.Register(LocCommands.Stub("indicator"), new CommandChangeIndicator(KittyRole.Admin, KittyRating.Safe));
		
		manager.Register(LocCommands.Stub("poll"), new CommandPollManage(KittyRole.Mod, KittyRating.Safe));
		manager.Register(LocCommands.Stub("givebeans"), new CommandGiveBeans(KittyRole.Mod, KittyRating.Safe));
		manager.Register(LocCommands.Stub("rpg"), new CommandRPG(KittyRole.Mod, KittyRating.Safe));

		manager.Register(LocCommands.Stub("teey"), new CommandTeey(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("perish, thenperish"), new CommandPerish(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("yeet"), new CommandYeet(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("ping"), new CommandPing(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("boop"), new CommandBoop(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("roll"), new CommandRoll(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("choose"), new CommandChoose(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("help"), new CommandHelp(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("info, about"), new CommandInfo(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("vote"), new CommandPollVote(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("results"), new CommandPollResults(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("showpoll"), new CommandPollShow(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("wolfram"), new CommandWolfram(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("c++, g++, cplus, cpp"), new CommandColiru(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("java, jdoodle"), new CommandJDoodle(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("beans"), new CommandBeansShow(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("role"), new CommandRole(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("bet"), new CommandBetBeans(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("map"), new CommandMap(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("rpstart"), new CommandRPStart(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("rpend"), new CommandRPEnd(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("tony, stark, dontfeelgood, dontfeelsogood"), new CommandStark(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("blur"), new CommandBlurry(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("eightball, 8ball"), new CommandEightBall(KittyRole.General, KittyRating.Safe));
		manager.Register(LocCommands.Stub("defaultdance"), new CommandDefaultDance(KittyRole.General, KittyRating.Safe));
		
		manager.Register(LocCommands.Stub("derp"), new CommandDerpi(KittyRole.General, KittyRating.Filtered));
		manager.Register(LocCommands.Stub("e621"), new CommandE621(KittyRole.General, KittyRating.Filtered));
		
		return manager;
	}
	
	// Default database manager construction. It can be constructed 
	// in different ways, and so we construct it outside of the constructor for 
	// the factory  since it doesn't have to be present / can be elsewhere. 
	// Effectively we cache the database here.
	public static DatabaseManager ConstructDatabaseManager()
	{
		LazyInit();
		
		if(database == null)
			database = new DatabaseManager();
		
		return database;
	}
	
	public static Stats ConstructStats(CommandManager manager)
	{
		LazyInit();
		
		if(stats == null)
			stats = new Stats(manager);
		
		return stats;
	}
	
	public static RPManager ConstructRPManager()
	{
		LazyInit();
		
		if(rpManager == null)
			rpManager = new RPManager();
		
		return rpManager;
	}
	
	public static Integer GetGuildCount()
	{ 	synchronized(guildCache)
		{
			return guildCache.size();
		}
	}
	
	public static Integer GetUserCount()
	{ 	synchronized(userCache)
		{
			return userCache.size();
		}
	}
}
