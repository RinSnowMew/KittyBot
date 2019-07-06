package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import utils.GlobalLog;
import utils.LogFilter;

// While some of the patterns in this file are similar to the localization files,
// commands that are being looked up will behave slightly differently so trimming
// rules for this file are different than the localization ones - this is more 
// aggresive with whitespace removal.
public class CommandEnabler implements IConfigSection
{
	// Config/const variables
	public static final String enabled = "1";
	public static final String disabled = "0";
	public static final boolean defaultEnabledState = true;
	public static final String commandSplit = ",";
	public static final String HeaderName = "Command Enabler";
	
	// Local variables
	private HashMap<String, Boolean> enabledMap; // Quick lookup
	private HashMap<String, String> likeNames;	 // Single names mapped to multiple names for use in enabledMap.
	private ArrayList<String> keyList;			 // Tracking ordering for later
	
	// Instance
	public static CommandEnabler instance;
	
	// Constructor
	public CommandEnabler()
	{
		super();
		
		if(instance == null)
		{
			// Create/Init variables
			GlobalLog.log(LogFilter.Core, "Initializing " + this.getClass().getSimpleName());
			enabledMap = new HashMap<>();
			keyList = new ArrayList<>();
			likeNames = new HashMap<String, String>();
			
			instance = this;
		}
		else
		{
			GlobalLog.error(LogFilter.Core, "You can't have two of the following: " + this.getClass().getSimpleName());
		}
	}
	
	// Reads in the config file and parses it, keeping tabs on the order it read things
	private void readIn(List<ConfigItem> items)
	{
		keyList.clear();
		enabledMap.clear();
		
		for(ConfigItem item : items)
		{
			String key = item.key;
			String value = item.value;
			
			// Keep track of the key itself
			keyList.add(key);
			
			// Write if it's enabled or disabled.
			if(value.equalsIgnoreCase(enabled))
			{
				enabledMap.putIfAbsent(key, true);
			}
			else
			{
				enabledMap.putIfAbsent(key, false);
			}
			
			// Keep track of alternate names
			String[] keys = key.split(commandSplit);
			for(int i = 0; i < keys.length; ++i)
			{
				likeNames.putIfAbsent(keys[i], key);
			}
		}
	}
	
	// Look up the already scraped values from the localizer and store them if they
	// don't already exist in the lookup. Defaults to defaultEnabledState.
	private void getTrackedCommands()
	{
		ArrayList<String> unloc = LocCommands.getUnlocalizedCommands();
		
		for(int i = 0; i < unloc.size(); ++i)
		{
			String command = unloc.get(i).toLowerCase();
			
			if(enabledMap.putIfAbsent(command, defaultEnabledState) == null)
			{
				GlobalLog.log(LogFilter.Strings, "Identified new toggleable raw command: " + command);
				keyList.add(command);
			}
		}
	}
	
	// Write out enabled/disabled file info.
	private List<ConfigItem> writeOut()
	{
		// Parse in original format
		List<ConfigItem> configItems = new Vector<ConfigItem>();
		
		for(int i = 0; i < keyList.size(); ++i)
		{
			String key = keyList.get(i).toLowerCase();
			String value = enabled.toLowerCase();
			
			if(enabledMap.get(key) == false)
			{
				value = disabled.toLowerCase();
			}
			
			configItems.add(new ConfigItem(HeaderName, key, value));
		}
		
		Collections.sort(configItems, (c1, c2) -> { return c1.key.compareTo(c2.key); });

		return configItems;
	}
	
	// Looks up a key to see if it's enabled or not
	public boolean isEnabled(String key)
	{
		String toCheck = key.toLowerCase();
		
		// Look up to see if we have the key anywhere. If not, default to enabled.
		// It should be noted that this is just checking for disabled commands, we don't
		// determine if a key is actually a real command in here or not.
		String fullKey = likeNames.getOrDefault(toCheck, null);
		if(fullKey == null)
			return true; 
		
		// If we have it, look it up. If we can't find it, default to true.
		return enabledMap.getOrDefault(fullKey, true);
	}

	@Override
	public String getSectionTitle()
	{
		return HeaderName;
	}

	@Override
	public void consume(List<ConfigItem> pairs)
	{
		readIn(pairs);
		getTrackedCommands();
	}

	@Override
	public List<ConfigItem> produce()
	{
		return writeOut();
	}
}
