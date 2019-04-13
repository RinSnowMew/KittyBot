package core;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import core.CommandManager.ThreadData;
import utils.GlobalLog;
import utils.LogFilter;

// This is a class designed to be asked about various kittybot stats.
// It follows a singleton pattern, and is accessable from anywhere internally.
public class Stats
{
	public static String botName = "KittyBot";
	public static Stats instance = null;
	
	// Internal
	private boolean isShuttingDown;
	private long messagesSeen;
	private long initTimeMS;
	
	private CommandManager commandManager;
	private OperatingSystemMXBean osBean;
	
	public Stats(CommandManager manager)
	{
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			GlobalLog.Error(LogFilter.Core, "Attempted to create a second Stats singleton!");
			return;
		}
		
		isShuttingDown = false;
		messagesSeen = 0;
		initTimeMS = System.currentTimeMillis();
		
		commandManager = manager;
		osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	}
	
	public void NoteMessageEvent()
	{
		++messagesSeen;
	}
	
	public void IndicateShutdown()
	{
		synchronized(instance)
		{
			isShuttingDown = true;
		}
	}
	
	public boolean GetIsShuttingDown()
	{
		synchronized(instance)
		{
			return isShuttingDown;
		}
	}
	
	  /////////////////////////////////////////
	 // All the functions to look up stats! //
	/////////////////////////////////////////
	
	// Formatted as HH:MM:SS
	public String GetFormattedUptime()
	{
		long dif = System.currentTimeMillis() - initTimeMS;
		
		return String.format("%02d:%02d:%02d", 
			TimeUnit.MILLISECONDS.toHours(dif),
			TimeUnit.MILLISECONDS.toMinutes(dif) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dif)),
			TimeUnit.MILLISECONDS.toSeconds(dif) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dif)));   
	}
	
	// Get number of commands that kitty has run!
	public long GetCommandsProcessed()
	{
		return commandManager.GetInvokeCount();
	}
	
	public long GetMessagesSeen()
	{
		return messagesSeen;
	}
	
	public double GetSystemCPULoad()
	{
		return osBean.getSystemLoadAverage();
	}
	
	public long GetCPUAvailable() 
	{
		return osBean.getAvailableProcessors();
	}

	public ThreadData GetThreadData()
	{
		return commandManager.DumpThreadData();
	}
	
	public int GetGuildCount()
	{
		return ObjectBuilderFactory.GetGuildCount();
	}
	
	public int GetUserCount()
	{
		return ObjectBuilderFactory.GetUserCount();
	}
	
	public ArrayList<Command> GetAllCommands()
	{
		return commandManager.GetAllRegisteredCommands();
	}
	
	public String GetHelpText(String commandName)
	{
		return commandManager.GetCommandHelpText(commandName);
	}
}
