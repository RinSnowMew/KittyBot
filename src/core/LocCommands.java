package core;

import java.util.ArrayList;
import utils.GlobalLog;
import utils.LogFilter;
import utils.io.FileMonitor;
import dataStructures.Pair;

// Performs the same localization for the strings associated with command names as 
// is performed with general strings in the application
public class LocCommands extends BaseLocFile
{
	public static final String fileName = "locCommands.config";
	public static final String function = "LocCommands.Stub";
	
	private static LocCommands instance;
	
	public LocCommands() 
	{
		super(fileName, function);
		
		GlobalLog.Log(LogFilter.Core, "Initializing " + this.getClass().getSimpleName());
		
		if(instance == null)
		{
			instance = this;
			
			UpdateLocFromDisk();
			ScrapeAll();
			SaveLocToDisk();
			
			fileMonitor = new FileMonitor(filename);
		}
		else
		{
			GlobalLog.Error(LogFilter.Core, "You can't have two of the following: " + this.getClass().getSimpleName());
		}
	}

	// Returns a pair, the raw key and the stub key
	public static Pair<String, String> Stub(String toStub)
	{
		return new Pair<String, String>(toStub, instance.GetKey(toStub));
	}
	
	// Gets all of the un-translated defaults in the commands list.
	public static ArrayList<String> GetUnlocalizedCommands()
	{
		ArrayList<String> raw = new ArrayList<>();
		instance.stringStore.ForEach((pair) -> raw.add((String)((Pair<?, ?>)pair).First ));
		return raw;
	}
	
	public static void Upkeep()
	{
		instance.Update();
	}
}
