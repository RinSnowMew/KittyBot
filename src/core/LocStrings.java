package core;

import utils.GlobalLog;
import utils.LogFilter;

// A quick-and-dirty localization tool that scrapes the project for calls to itself, then
// generates/updates a file externally (phrases.config) with all the stub values as keys that 
// can then be localized.
public class LocStrings extends LocBase
{
	public static final String fileName = "locStrings.config";
	public static final String function = "LocStrings.Stub";
	
	private static LocStrings instance;
	
	public LocStrings()
	{
		super(fileName, function);
		
		GlobalLog.Log(LogFilter.Core, "Initializing " + this.getClass().getSimpleName());
		
		if(instance == null)
		{
			instance = this;
			
			UpdateLocFromDisk();
			ScrapeAll();
			SaveLocToDisk();
		}
		else
		{
			GlobalLog.Error(LogFilter.Core, "You can't have two of the following: " + this.getClass().getSimpleName());
		}
	}

	public static String Stub(String toStub)
	{
		return instance.GetKey(toStub);
	}
}