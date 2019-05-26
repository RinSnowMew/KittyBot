package core;

import java.util.Date;
import java.util.Vector;
import utils.GlobalLog;
import utils.LogFilter;

public class DatabaseManager
{
	// Singleton accessor
	public static DatabaseManager instance = null; 

	// Private internal variables
	private Vector<DatabaseTrackedObject> trackedObjects;
	private DatabaseDriver driver;
	private Date lastUpkeep;
	
	public DatabaseManager()
	{
		GlobalLog.Log(LogFilter.Database, "Creating database manager");
		
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			GlobalLog.Error(LogFilter.Database, "Attempted to register a second DataBase manager!");
			return;
		}
		
		lastUpkeep = new Date();
		trackedObjects = new Vector<DatabaseTrackedObject>();
		driver = new DatabaseDriver();
		
		if(driver.Connect() == false)
		{
			GlobalLog.Error("Database failed to connect. Currently, without the DB, this bot can not run.");
			System.exit(1);
		}
	}
	
	// Thumbs through registered objects and syncs them with the database. 
	// Consider moving this operation to a separate thread.
	public int Upkeep()
	{
		synchronized(trackedObjects)
		{
			int numUpdated = 0;
			
			for(int i = 0 ; i < trackedObjects.size(); ++i)
			{
				DatabaseTrackedObject dto = trackedObjects.get(i);
				
				if(dto.IsDirty())
				{
					SetRemoteValue(dto.identifier, dto.Serialize());
					dto.Resolve();
					++numUpdated;
				}
			}
			
			lastUpkeep = new Date();
			
			return numUpdated;
		}
	}
	
	public void Register(DatabaseTrackedObject tracked)
	{
		synchronized(trackedObjects)
		{
			trackedObjects.add(tracked);
			tracked.DeSerialzie(GetRemoteValue(tracked.identifier));
		}
	}
	
	public String GetRemoteValue(String key)
	{
		synchronized(driver)
		{
			return driver.CreateGetKey(key);
		}
	}
	
	public void SetRemoteValue(String key, String value)
	{
		synchronized(driver)
		{
			driver.CreateSetKey(key, value);
		}
	}
	
	public Date GetLastUpkeep()
	{
		return lastUpkeep;
	}
	
	public int GetTrackedObjectsSize()
	{
		return trackedObjects.size();
	}
}
