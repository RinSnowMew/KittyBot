package core;

import java.util.ArrayList;

import utils.GlobalLog;
import utils.LogFilter;
import dataStructures.Pair;

// Performs the same localization for the strings associated with command names as 
// is performed with general strings in the application
public class LocCommands extends BaseLocFile implements IConfigSection
{
	// Defined const variables
	public static final String HeaderName = "Localized Commands";
	public static final String function = "LocCommands.stub";
	
	// Instance
	private static LocCommands instance;
	
	// Constructor
	public LocCommands() 
	{
		super(HeaderName, function);
		
		GlobalLog.log(LogFilter.Core, "Initializing " + this.getClass().getSimpleName());
		
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			GlobalLog.error(LogFilter.Core, "You can't have two of the following: " + this.getClass().getSimpleName());
		}
	}

	// Returns a pair, the raw key and the stub key
	public static Pair<String, String> stub(String toStub)
	{
		return new Pair<String, String>(toStub, instance.getKey(toStub));
	}
	
	// Gets all of the un-translated defaults in the commands list.
	public static ArrayList<String> getUnlocalizedCommands()
	{
		ArrayList<String> raw = new ArrayList<>();
		instance.localized.keySet().forEach((key) -> raw.add(key));
		return raw;
	}
}
