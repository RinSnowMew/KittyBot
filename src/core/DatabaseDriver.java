package core;

import java.sql.ResultSet;
import java.sql.SQLException;

import network.JDBCDriver;
import network.JDBCDriverSQLite;
import network.JDBCStatementType;
import utils.GlobalLog;
import utils.LogFilter;

// Java DataBase Connection Driver - the generic version.
// This is where we put everything and swap out the moving parts, 
// ie MySQL, PostgreSQL, SQLite, etc...
//
// Right now this can be messed up with SQL injection stuff if users
// are allowed to directly touch data. Leaving it like this temporarily, 
// and is for prototyping only!
public class DatabaseDriver 
{
	// Config and local varaibles
	private JDBCDriver driver;
	private final String globalTableName = "kitty_globals";
	private final String globalKeyName = "GlobalKey";
	private final String globalValueName = "GlobalValue";
	
	public DatabaseDriver()
	{
		driver = null;
	}
	
	// Makes sure a table exists with the specified name.
	// The keyName specifies the key column label, and the valueName specifies the value column label.
	public void EnsureTableExists(String tableName, String keyName, String valueName)
	{
		// Require a global table if it doesn't exist already
		driver.ExecuteStatement(JDBCStatementType.Create, "CREATE TABLE IF NOT EXISTS " + tableName + " (" + keyName + " text PRIMARY KEY, " + valueName + " text)", null);
	}
	
	// Set up and create a table in the database
	public boolean Connect()
	{
		driver = new JDBCDriverSQLite();
		if(driver.Connect() == false)
		{
			return false;
		}
	
		// Verify tables we want to use exist
		EnsureTableExists(globalTableName, globalKeyName, globalValueName); // General table. Do not remove.
		
		return true;
	}
	
	// The key will be created if it doesn't exist and the value specified will be stored.
	public void CreateSetKey(String key, String value)
	{
		GlobalLog.Log(LogFilter.Database, "CreateSetKey: Key-" + key + " value-" + value);
		
		if(HasKey(key))
		{
			UpdateKey(key, value);
		}
		else
		{
			CreateKey(key, value);
		}
	}
	
	// Creates a key. The key will be created if it doesn't exist, and the default value returned.
	public String CreateGetKey(String key)
	{
		GlobalLog.Log(LogFilter.Database, "CreateGeyKey: key-" + key);
		
		if(HasKey(key))
		{
			return GetKey(key);
		}
		else
		{
			String newValue = "";
			CreateKey(key, newValue);
			return newValue;
		}
	}
	
	// Prototype formatting for key updating
	private void UpdateKey(String key, String value)
	{
		String command = "UPDATE " + globalTableName + " SET " + globalValueName + " = ? WHERE " + globalKeyName + " = ?";
		boolean status = driver.ExecuteStatement(JDBCStatementType.Update, command, new String[] { value, key });
		GlobalLog.Log(LogFilter.Database, "UpdateKey status: " + status);
	}
	
	// Protoype updating for seeing if a key exists
	private boolean HasKey(String key)
	{
		String command = "SELECT COUNT(1) as count FROM " + globalTableName + " WHERE " + globalKeyName + " = ?";
		ResultSet set = driver.ExecuteReturningStatement(JDBCStatementType.Select, command, new String[] { key });
		String out = ResultAsString(set, "count");
		return out.charAt(0) == '1';
	}
	
	// Prototype for getting a key
	private String GetKey(String key)
	{
		String command = "SELECT " + globalValueName + " as searchedKey FROM " + globalTableName +" WHERE " + globalKeyName + " = ?";
		ResultSet set = driver.ExecuteReturningStatement(JDBCStatementType.Select, command, new String[] { key });
		return ResultAsString(set, "searchedKey");
	}
	
	// Prototype for creating a key
	private void CreateKey(String key, String value)
	{
		String command = "INSERT INTO " + globalTableName + " (GlobalKey, GlobalValue) VALUES (?, ?)";
		boolean status = driver.ExecuteStatement(JDBCStatementType.Insert, command, new String[] { key, value });
		GlobalLog.Log(LogFilter.Database, "CreateKey status: " + status);
	}
	
	// Transforms a result into a string if possible.
	private String ResultAsString(ResultSet rs, String key)
	{
		if(rs == null)
			return "";
		
		try
		{
			boolean hasKey = rs.next();
			if(hasKey)
			{
				String val = rs.getString(key);
				return val;
			}
			else
				return null;
				
		}
		catch (SQLException e) 
		{
			GlobalLog.Error(LogFilter.Database, e.toString());
			return null;
		}	
	}
}