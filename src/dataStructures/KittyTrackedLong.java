package dataStructures;

import core.DatabaseTrackedObject;
import utils.GlobalLog;
import utils.LogFilter;

// An automatically tracked long!
// Usage example: 
// 
// KittyTrackedLong score;
//
// public SomeConstructor(...)
// {
//     myValue = new KittyTrackedLong("friendlyName", this.UniqueID);
// }
//
public class KittyTrackedLong extends DatabaseTrackedObject 
{
	private long tracked;            // Tracked value
	private final String databaseID; // in-database ID of this value
	
	// Config
	private final static String differentiator = "long-";
	
	public KittyTrackedLong(String readableName, String UniqueID)
	{
		super(differentiator + readableName + UniqueID);
		databaseID = differentiator + readableName + UniqueID;
	}
	
	public long Add(long toAdd)
	{
		tracked += toAdd;
		MarkDirty();
		return tracked;
	}
	
	public long Subtract(long toSubtract)
	{
		tracked -= toSubtract;
		MarkDirty();
		return tracked;
	}

	public long Get()
	{
		return tracked;
	}
	
	public long Set(long newValue)
	{
		tracked = newValue;
		MarkDirty();
		return tracked;
	}
	
	@Override
	public String Serialize()
	{
		return "" + tracked;
	}

	@Override
	public void DeSerialzie(String string)
	{
		if(string != null)
		{
			try
			{
				tracked = Long.parseLong(string);
			}
			catch(Exception e)
			{
				tracked = 0;
				GlobalLog.Error(LogFilter.Core, "Failed to deserialize long with database ID " + databaseID);
			}
		}
		else
		{
			tracked = 0;
		}
	}
}
