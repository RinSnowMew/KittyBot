package dataStructures;

import core.DatabaseTrackedObject;
import utils.GlobalLog;
import utils.LogFilter;

public class KittyTrackedString extends DatabaseTrackedObject
{
	private String trackedString;                           // This is the string tracked in the database
	private final static String differentiator = "string-"; // Lends a very slightly human-readable label to the database key
	
	// Constructor with unique id and readable name
	public KittyTrackedString(String readableName, String UniqueID)
	{
		super(differentiator + readableName + UniqueID);
	}

	// Sets the value of the string and marks it as dirty
	public void set(String newValue)
	{
		synchronized(trackedString)
		{
			trackedString = newValue;
			markDirty();
		}
	}
	
	// Returns string. The string is copied because it is immutable. 
	public String get()
	{
		synchronized(trackedString)
		{
			return trackedString;
		}
	}
	
	@Override
	public String serialize() 
	{
		return trackedString;
	}

	@Override
	public void deSerialzie(String string)
	{
		if(string != null && !string.isEmpty())
		{
			trackedString = string;
		}
		else
		{
			GlobalLog.log(LogFilter.Database, "String had null or empty value with identifier: " + identifier); 
			string = "";
			markDirty();
		}
	}

}
