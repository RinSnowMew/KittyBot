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
			GlobalLog.error(LogFilter.Core, "Attempted to create a second Stats singleton!");
			return;
		}
		
		isShuttingDown = false;
		messagesSeen = 0;
		initTimeMS = System.currentTimeMillis();
		
		commandManager = manager;
		osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	}
	
	public void noteMessageEvent()
	{
		++messagesSeen;
	}
	
	public void indicateShutdown()
	{
		synchronized(instance)
		{
			isShuttingDown = true;
		}
	}
	
	public boolean getIsShuttingDown()
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
	public String getFormattedUptime()
	{
		long dif = System.currentTimeMillis() - initTimeMS;
		
		return String.format("%02d:%02d:%02d", 
			TimeUnit.MILLISECONDS.toHours(dif),
			TimeUnit.MILLISECONDS.toMinutes(dif) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(dif)),
			TimeUnit.MILLISECONDS.toSeconds(dif) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(dif)));   
	}
	
	// Get number of commands that kitty has run!
	public long getCommandsProcessed()
	{
		return commandManager.getInvokeCount();
	}
	
	public long getMessagesSeen()
	{
		return messagesSeen;
	}
	
	public double getSystemCPULoad()
	{
		return osBean.getSystemLoadAverage();
	}
	
	public long getCPUAvailable() 
	{
		return osBean.getAvailableProcessors();
	}

	public ThreadData getThreadData()
	{
		return commandManager.dumpThreadData();
	}
	
	public int getGuildCount()
	{
		return ObjectBuilderFactory.getGuildCount();
	}
	
	public int getUserCount()
	{
		return ObjectBuilderFactory.getUserCount();
	}
	
	public ArrayList<Command> getAllCommands()
	{
		return commandManager.getAllRegisteredCommands();
	}
	
	public String getHelpText(String commandName)
	{
		return commandManager.getCommandHelpText(commandName);
	}
}
