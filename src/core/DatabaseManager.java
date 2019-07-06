package core;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import utils.GlobalLog;
import utils.LogFilter;

// This wrangles all of the differentiated storage systems. Internally, these may be tables,
// different databases, etc -- the goal is that it's abstracted away.
//
// TODO: Implement intermediate structure on account of duplicate patterns showing up.
public class DatabaseManager
{
	// Singleton accessor
	public static DatabaseManager instance = null; 

	// Database names and configs
	private final String globalTableName = "kitty_globals";     // Table name
	private final String globalKeyColumnName = "GlobalKey";     // Column name for the key 
	private final String globalValueColumnName = "GlobalValue"; // Column name for the value
	
	private final String characterTableName = "kitty_characters";     // Table name
	private final String characterKeyColumnName = "CharacterKey";     // Column name for the key 
	private final String characterValueColumnName = "CharacterValue"; // Column name for the value
	
	
	// Private internal variables
	private Vector<DatabaseTrackedObject> globalDataTrackedObjects;
	private Vector<DatabaseTrackedObject> characterDataTrackedObjects;
	private DatabaseDriverKeyValue globalDataDriver;
	private DatabaseDriverKeyValue characterDataDriver;
	private Date lastUpkeep;
	
	// Constructor to enforce singleton.
	public DatabaseManager()
	{
		GlobalLog.log(LogFilter.Database, "Creating database manager");
		
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			GlobalLog.error(LogFilter.Database, "Attempted to register a second DataBase manager!");
			return;
		}
		
		// Initialize variables
		lastUpkeep = new Date();
		globalDataTrackedObjects = new Vector<DatabaseTrackedObject>();
		characterDataTrackedObjects = new Vector<DatabaseTrackedObject>();
		
		// Initialize data sets
		globalDataDriver = new DatabaseDriverKeyValue(globalTableName, globalKeyColumnName, globalValueColumnName);
		characterDataDriver = new DatabaseDriverKeyValue(characterTableName, characterKeyColumnName, characterValueColumnName);
		
		// Connect data sets
		if(globalDataDriver.connect() == false)
		{
			GlobalLog.error("Global database failed to connect. Without this DB, this bot can not run.");
			System.exit(1);
		}
		
		if(characterDataDriver.connect() == false)
		{
			GlobalLog.error("Character database failed to connect. Without this DB, this bot can not run.");
			System.exit(1);
		}
	}
	
	// Thumbs through registered objects and syncs them with the database. 
	// Consider moving this operation to a separate thread.
	public int upkeep()
	{
		int total = 0;
		
		total += globalDataUpkeep();
		total += characterDataUpkeep();
		
		lastUpkeep = new Date();
		return total;
	}
	
	// Global data management and updating, returns how many items were updated.
	private int globalDataUpkeep()
	{
		synchronized(globalDataTrackedObjects)
		{
			int numUpdated = 0;
			
			for(int i = 0; i < globalDataTrackedObjects.size(); ++i)
			{
				DatabaseTrackedObject dto = globalDataTrackedObjects.get(i);
				
				if(dto.isDirty())
				{
					globalSetRemoteValue(dto.identifier, dto.serialize());
					dto.resolve();
					++numUpdated;
				}
			}
			
			return numUpdated;
		}
	}
	
	// Character data management and updating, returns how many items were updated.
	private int characterDataUpkeep()
	{
		synchronized(characterDataTrackedObjects)
		{
			int numUpdated = 0;
			
			for(int i = 0; i < characterDataTrackedObjects.size(); ++i)
			{
				DatabaseTrackedObject dto = characterDataTrackedObjects.get(i);
				
				if(dto.isDirty())
				{
					characterSetRemoteValue(dto.identifier, dto.serialize());
					dto.resolve();
					++numUpdated;
				}
			}
			
			return numUpdated;
		}
	}
	
	public List<String> scrapeGlobalForString(String substring)
	{
		return globalDataDriver.getKeysWith(substring);
	}
	
	  /////////////////
	 // Global Data //
	/////////////////
	public void globalRegister(DatabaseTrackedObject tracked)
	{
		synchronized(globalDataTrackedObjects)
		{
			globalDataTrackedObjects.add(tracked);
			tracked.deSerialzie(globalGetRemoteValue(tracked.identifier));
		}
	}
	
	// You can get values, but not modify them.
	public String globalGetRemoteValue(String key)
	{
		synchronized(globalDataDriver)
		{
			return globalDataDriver.createGetKey(key);
		}
	}
	
	private void globalSetRemoteValue(String key, String value)
	{
		synchronized(globalDataDriver)
		{
			globalDataDriver.createSetKey(key, value);
		}
	}
	
	
	  ////////////////////
	 // Character Data //
	////////////////////
	public void characterRegister(DatabaseTrackedObject tracked)
	{
		synchronized(characterDataTrackedObjects)
		{
			characterDataTrackedObjects.add(tracked);
			tracked.deSerialzie(characterGetRemoteValue(tracked.identifier));
		}
	}
	
	// You can get values, but not modify them.
	public String characterGetRemoteValue(String key)
	{
		synchronized(characterDataDriver)
		{
			return characterDataDriver.createGetKey(key);
		}
	}
	
	private void characterSetRemoteValue(String key, String value)
	{
		synchronized(characterDataDriver)
		{
			characterDataDriver.createSetKey(key, value);
		}
	}
	
	
	  ///////////////////////
	 // Utility functions //
	///////////////////////
	public Date getLastUpkeep()
	{
		return lastUpkeep;
	}
	
	public int getTrackedObjectsSize()
	{
		return globalDataTrackedObjects.size();
	}
}
