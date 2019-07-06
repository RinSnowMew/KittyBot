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
//     DatabaseManager.instance.Register(myValue);
// }
//
public class KittyTrackedLong extends DatabaseTrackedObject 
{
	private long tracked; // Tracked value
	
	// Config
	private final static String differentiator = "long-";
	
	public KittyTrackedLong(String readableName, String UniqueID)
	{
		super(differentiator + readableName + UniqueID);
	}
	
	public long add(long toAdd)
	{
		tracked += toAdd;
		markDirty();
		return tracked;
	}
	
	public long subtract(long toSubtract)
	{
		tracked -= toSubtract;
		markDirty();
		return tracked;
	}

	public long get()
	{
		return tracked;
	}
	
	public long set(long newValue)
	{
		tracked = newValue;
		markDirty();
		return tracked;
	}
	
	@Override
	public String serialize()
	{
		return "" + tracked;
	}

	@Override
	public void deSerialzie(String string)
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
				GlobalLog.error(LogFilter.Core, "Falling back to default of 0 due to failure to deserialize long with database ID " + identifier);
			}
		}
		else
		{
			tracked = 0;
		}
	}
}
