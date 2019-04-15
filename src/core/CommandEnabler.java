package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utils.FileUtils;
import utils.GlobalLog;
import utils.LogFilter;

// While some of the patterns in this file are similar to the localization files,
// commands that are being looked up will behave slightly differently so trimming
// rules for this file are different than the localization ones - this is more 
// aggresive with whitespace removal.
public class CommandEnabler
{
	// Config/const variables
	public static final String filename = "commands.config";
	public static final String pairSplit = "=";
	public static final char pairSeparator = '\n';
	public static final String enabled = "1";
	public static final String disabled = "0";
	public static final boolean defaultEnabledState = true;
	
	// Local variables
	private HashMap<String, Boolean> enabledMap; // Quick lookup
	private ArrayList<String> keyList; // Tracking ordering for later
	
	public CommandEnabler()
	{
		GlobalLog.Log(LogFilter.Core, "Initializing " + this.getClass().getSimpleName());
		enabledMap = new HashMap<>();
		keyList = new ArrayList<>();
		
		ReadIn();
		GetTrackedCommands();
		WriteOut();
	}
	
	// Reads in the config file and parses it, keeping tabs on the order it read things
	private void ReadIn()
	{
		File f = new File(filename);
		if(f.isFile() && f.canRead())
		{
			String content = FileUtils.ReadContent(f).trim();
			String[] lines = content.split("" + pairSeparator);
			
			for(int i = 0; i < lines.length; ++i)
			{
				String[] pair = lines[i].split(pairSplit);
				
				if(pair.length < 2)
					continue;
				
				String key = pair[0].trim();
				String value = pair[1].trim().toLowerCase();
				
				keyList.add(key);

				if(value.equalsIgnoreCase(enabled))
					enabledMap.putIfAbsent(key, true);
				else
					enabledMap.putIfAbsent(key, false);
			}
		}
	}
	
	// Look up the already scraped values from the localizer and store them if they
	// don't already exist in the lookup. Defaults to defaultEnabledState.
	private void GetTrackedCommands()
	{
		ArrayList<String> unloc = LocCommands.GetUnlocalizedCommands();
		
		for(int i = 0; i < unloc.size(); ++i)
		{
			String command = unloc.get(i);
			if(enabledMap.putIfAbsent(command, defaultEnabledState) == null)
			{
				GlobalLog.Log(LogFilter.Strings, "Identified new toggleable raw command: " + command);
				keyList.add(command);
			}
		}
	}
	
	// Write out enabled/disabled file info.
	private void WriteOut()
	{
		try
		{
			String outString = "";
			for(int i = 0; i < keyList.size(); ++i)
			{
				String key = keyList.get(i);
				String value = enabled;
				
				if(enabledMap.get(key) == false)
					value = disabled;
				
				outString += key + pairSplit + value + pairSeparator;
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(outString);
			writer.close();
		}
		catch (IOException e)
		{
			GlobalLog.Error(LogFilter.Core, "Command enabler issue writing file! " + e.getMessage());
		}
	}
	
	// Looks up a key to see if it's enabled or not
	public boolean IsEnabled(String key)
	{
		if(enabledMap.containsKey(key))
			return enabledMap.get(key);
		
		return true;
	}
}
