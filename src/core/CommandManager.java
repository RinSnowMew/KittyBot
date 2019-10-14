package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import commands.characters.CommandCharacterMain;
import commands.general.CommandBeansShow;
import commands.general.CommandBenchmark;
import commands.general.CommandBetBeans;
import commands.general.CommandBetHistory;
import commands.general.CommandBlurry;
import commands.general.CommandBoop;
import commands.general.CommandCatch;
import commands.general.CommandChangeIndicator;
import commands.general.CommandChoose;
import commands.general.CommandColiru;
import commands.general.CommandColor;
import commands.general.CommandCrouton;
import commands.general.CommandDBFlush;
import commands.general.CommandDBStats;
import commands.general.CommandDerpi;
import commands.general.CommandDoWork;
import commands.general.CommandE621;
import commands.general.CommandEightBall;
import commands.general.CommandFetch;
import commands.general.CommandGiveBeans;
import commands.general.CommandHelp;
import commands.general.CommandHelpBuilder;
import commands.general.CommandInfo;
import commands.general.CommandInvite;
import commands.general.CommandJDoodle;
import commands.general.CommandLeaderboard;
import commands.general.CommandMap;
import commands.general.CommandPerish;
import commands.general.CommandPing;
import commands.general.CommandRPEnd;
import commands.general.CommandRPG;
import commands.general.CommandRPStart;
import commands.general.CommandRating;
import commands.general.CommandRemind;
import commands.general.CommandRole;
import commands.general.CommandRoll;
import commands.general.CommandShutdown;
import commands.general.CommandStark;
import commands.general.CommandStats;
import commands.general.CommandTeey;
import commands.general.CommandTweet;
import commands.general.CommandWolfram;
import commands.general.CommandYeet;
import commands.guildrole.CommandGuildRoleMain;
import commands.mod.CommandModMain;
import commands.music.CommandMusicMain;
import commands.poll.CommandPollMain;
import commands.raffle.CommandRaffleMain;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Pair;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.GlobalLog;
import utils.LogFilter;

public class CommandManager 
{
	// Variables
	private HashMap<String, Command> commands;
	private ArrayList<CommandThread> threadAccumulator;
	private CommandEnabler commandEnabler;
	private long invokeCount;
	
	// Singleton
	public static CommandManager instance;
	
	// Default Constructor
	public CommandManager(CommandEnabler commandEnabler)
	{
		GlobalLog.log(LogFilter.Core, "Initializing " + this.getClass().getSimpleName());
		
		if(instance == null)
		{
			this.commands = new HashMap<String, Command>();
			this.threadAccumulator = new ArrayList<CommandThread>();
			this.invokeCount = 0;
			this.commandEnabler = commandEnabler; 
			
			registerAllCommands();
			
			instance = this;
		}
		else
		{
			GlobalLog.error(LogFilter.Core, "You can't have two of the following: " + this.getClass().getSimpleName());
		}
	}
	
	// Registers all the commands for the instance
	public void registerAllCommands()
	{
		this.commands.clear();
		
		// Dev
		this.register(LocCommands.stub("work"), new CommandDoWork(KittyRole.Dev, KittyRating.Safe));
		this.register(LocCommands.stub("shutdown"), new CommandShutdown(KittyRole.Dev, KittyRating.Safe));
		this.register(LocCommands.stub("stats"), new CommandStats(KittyRole.Dev, KittyRating.Safe));
		this.register(LocCommands.stub("invite"), new CommandInvite(KittyRole.Dev, KittyRating.Safe));
		this.register(LocCommands.stub("buildHelp"), new CommandHelpBuilder(KittyRole.Dev, KittyRating.Safe));
		this.register(LocCommands.stub("tweet"), new CommandTweet(KittyRole.Dev, KittyRating.Safe));
		this.register(LocCommands.stub("dbflush"), new CommandDBFlush(KittyRole.Dev, KittyRating.Safe));
		this.register(LocCommands.stub("dbstats"), new CommandDBStats(KittyRole.Dev, KittyRating.Safe));
		
		// Admin
		this.register(LocCommands.stub("rating"), new CommandRating(KittyRole.Admin, KittyRating.Safe));
		this.register(LocCommands.stub("indicator"), new CommandChangeIndicator(KittyRole.Admin, KittyRating.Safe));
		
		// Mod
		this.register(LocCommands.stub("givebeans"), new CommandGiveBeans(KittyRole.Mod, KittyRating.Safe));
		this.register(LocCommands.stub("rpg"), new CommandRPG(KittyRole.Mod, KittyRating.Safe));
		this.register(LocCommands.stub("mod"), new CommandModMain(KittyRole.Mod, KittyRating.Safe));

		// General
		this.register(LocCommands.stub("fetch"), new CommandFetch(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("teey"), new CommandTeey(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("perish, thenperish"), new CommandPerish(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("yeet"), new CommandYeet(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("ping"), new CommandPing(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("boop"), new CommandBoop(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("roll"), new CommandRoll(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("choose"), new CommandChoose(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("help"), new CommandHelp(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("info, about"), new CommandInfo(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("wolfram"), new CommandWolfram(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("c++, g++, cplus, cpp"), new CommandColiru(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("java, jdoodle"), new CommandJDoodle(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("beans"), new CommandBeansShow(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("kittyrole"), new CommandRole(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("bet"), new CommandBetBeans(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("map"), new CommandMap(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("rpstart"), new CommandRPStart(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("rpend"), new CommandRPEnd(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("tony, stark, dontfeelgood, dontfeelsogood"), new CommandStark(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("blur"), new CommandBlurry(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("eightball, 8ball"), new CommandEightBall(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("catch"), new CommandCatch(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("bethistory"), new CommandBetHistory(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("crouton"), new CommandCrouton(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("benchmark, bench"), new CommandBenchmark(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("leaderboard"), new CommandLeaderboard(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("color, colour"), new CommandColor(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("remind"), new CommandRemind(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("role"), new CommandGuildRoleMain(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("character"), new CommandCharacterMain(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("raffle"), new CommandRaffleMain(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("poll"), new CommandPollMain(KittyRole.General, KittyRating.Safe));
		this.register(LocCommands.stub("music"), new CommandMusicMain(KittyRole.General, KittyRating.Safe));
		
		this.register(LocCommands.stub("e621"), new CommandE621(KittyRole.General, KittyRating.Filtered));
		this.register(LocCommands.stub("derpi"), new CommandDerpi(KittyRole.General, KittyRating.Filtered));
	}
	
	// Allows the command manager to keep track of a command. Takes a pair (the un-localized and localzied commands)
	// and the command associated with the localized commands.
	public void register(Pair<String, String> pair, Command command)
	{	
		if(pair == null || pair.Second == null)
			return;
		
		String[] keys = pair.Second.split(",");

		for(int i = 0; i < keys.length; ++i)
		{
			String key = keys[i].trim().toLowerCase();
			
			command.registeredNames.add(key);
			Command old = commands.put(key, command);
			
			if(old != null)
			{
				GlobalLog.warn(LogFilter.Core, "Writing over a command with the key " + key);
				return;
			}
			
			GlobalLog.log(LogFilter.Core, "Command registered under key " + key);
		}
	}
	
	// Calls the command but on a whole new thread!
	public boolean invokeOnNewThread(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response responseContext)
	{
		// This is here to prevent spinning up a thread if this wasn't even a command.
		if(input == null || !input.isValid())
			return false;
		
		// At this point, verify that the command is enabled. If we don't have any info
		// on if it's enabled or not, we assume it's enabled to prevent broken functionality due
		// to formatting or casing issues.
		if(input.key != null && !commandEnabler.isEnabled(input.key))
			return false;
		
		Command command = commands.get(input.key);
		if(command != null)
		{
			// Spin up a thread and begin it. The thread carries the info needed to invoke the command.
			CommandThread newThread = new CommandThread(this, input, guild, channel, user, responseContext);
			threadAccumulator.add(newThread);
			newThread.start();
			return true;
		}
		else
		{
			GlobalLog.warn(LogFilter.Command, "User " + user.name + " tried to invoke command that doesn't exist: " + input.key);
			return false;
		}
	}
	
	// Calls the command specified with the key, providing user information arguments, etc.
	void invoke(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response responseContext)
	{
		if(input == null || !input.isValid())
			return;
		
		Command command = commands.get(input.key);
		if(command != null)
		{
			++invokeCount;
			command.invoke(guild, channel, user, input, responseContext);
		}
	}
	
	// Looks up command by name. If it exists, dumps help text, otherwise returns null.
	public String getCommandHelpText(String key)
	{
		Command command = commands.get(key);
		
		if(command != null)
			return "`" + key + "`: " + command.getHelpText();
		
		return null;
	}
	
	// Returns the number of commands sent so far during this program run
	public long getInvokeCount()
	{
		return invokeCount;
	}
	
	// Returns all commands by name
	public ArrayList<Command> getAllRegisteredCommands()
	{
		ArrayList<Command> cmds = new ArrayList<Command>();
		for(Entry<String, Command> entry : commands.entrySet())
			cmds.add(entry.getValue());
		
		return cmds;
	}
	
	// Info about threads running packaged into an object
	public class ThreadData
	{
		public HashMap<Thread.State, Integer> states = new HashMap<Thread.State, Integer>();
	}
	public ThreadData dumpThreadData()
	{
		ThreadData data = new ThreadData();
		
		for(int i = 0; i < threadAccumulator.size(); ++i)
		{
			Thread.State state = threadAccumulator.get(i).getState();
			Integer num = data.states.get(state);
			
			if(num == null)
				num = 0;
			
			data.states.put(state, num + 1);
		}
		
		return data;
	}
}
