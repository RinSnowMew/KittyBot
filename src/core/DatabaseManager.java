package core;

import java.util.ArrayList;

import utils.GlobalLog;
import utils.LogFilter;

public class DatabaseManager
{
	// Singleton accessor
	public static DatabaseManager instance = null; 

	// Private internal variables
	private ArrayList<DatabaseTrackedObject> trackedObjects;
	private DatabaseDriver driver;
	
	public DatabaseManager()
	{
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			GlobalLog.Error(LogFilter.Database, "Attempted to register a second DataBase manager!");
			return;
		}
		
		trackedObjects = new ArrayList<DatabaseTrackedObject>();
		driver = new DatabaseDriver();
		driver.Connect();
	}
	
	// Thumbs through registered objects and syncs them with the database. 
	// Consider moving this operation to a separate thread.
	public void Upkeep()
	{
		for(int i = 0 ; i < trackedObjects.size(); ++i)
		{
			DatabaseTrackedObject dto = trackedObjects.get(i);
			
			if(dto.IsDirty())
			{
				SetRemoteValue(dto.identifier, dto.Serialize());
				dto.Resolve();
			}
		}
	}
	
	public void Register(DatabaseTrackedObject tracked)
	{
		trackedObjects.add(tracked);
		tracked.DeSerialzie(GetRemoteValue(tracked.identifier));
	}
	
	public String GetRemoteValue(String key)
	{
		return driver.CreateGetKey(key);
	}
	
	public void SetRemoteValue(String key, String value)
	{
		driver.CreateSetKey(key, value);
	}
}
