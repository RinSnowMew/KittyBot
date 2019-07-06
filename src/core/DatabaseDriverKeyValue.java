package core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import network.JDBCDriver;
import network.JDBCDriverSQLite;
import network.JDBCStatementType;
import utils.GlobalLog;
import utils.LogFilter;

// Java DataBase Connection Driver - the generic version.
// This is where we put everything and swap out the moving parts, 
// ie MySQL, PostgreSQL, SQLite, etc...
//
// This isn't overly flexible though, 
public class DatabaseDriverKeyValue
{
	// Config and local varaibles
	private JDBCDriver driver;
	private final String tableName;       // Table name
	private final String keyColumnName;   // Column name for the key 
	private final String valueColumnName; // Column name for the value
	
	public DatabaseDriverKeyValue(String tableName, String keyColumnName, String valueColumnName)
	{
		this.tableName = tableName;
		this.keyColumnName = keyColumnName;
		this.valueColumnName = valueColumnName;
		
		driver = null;
	}
	
	// Makes sure a table exists with the specified name.
	// The keyName specifies the key column label, and the valueName specifies the value column label.
	public void ensureTableExists(String tableName, String keyName, String valueName)
	{
		// Require a global table if it doesn't exist already
		driver.executeStatement(JDBCStatementType.Create, "CREATE TABLE IF NOT EXISTS " + tableName + " (" + keyName + " text PRIMARY KEY, " + valueName + " text)", null);
	}
	
	// Set up and create a table in the database
	public boolean connect()
	{
		driver = new JDBCDriverSQLite();
		if(driver.connect() == false)
		{
			return false;
		}
	
		// Verify tables we want to use exist
		ensureTableExists(tableName, keyColumnName, valueColumnName); // General table. Do not remove.
		
		return true;
	}
	
	// The key will be created if it doesn't exist and the value specified will be stored.
	public void createSetKey(String key, String value)
	{
		GlobalLog.log(LogFilter.Database, "CreateSetKey: Key-" + key + " value-" + value);
		
		if(hasKey(key))
		{
			updateKey(key, value);
		}
		else
		{
			createKey(key, value);
		}
	}
	
	// Creates a key. The key will be created if it doesn't exist, and the default value returned.
	public String createGetKey(String key)
	{
		GlobalLog.log(LogFilter.Database, "CreateGetKey: " + key);
		
		if(hasKey(key))
		{
			return getKey(key);
		}
		else
		{
			String newValue = "";
			createKey(key, newValue);
			return newValue;
		}
	}
	
	// Prototype formatting for key updating
	private void updateKey(String key, String value)
	{
		String command = "UPDATE " + tableName + " SET " + valueColumnName + " = ? WHERE " + keyColumnName + " = ?";
		boolean status = driver.executeStatement(JDBCStatementType.Update, command, new String[] { value, key });
		GlobalLog.log(LogFilter.Database, "UpdateKey status: " + status);
	}
	
	// Protoype updating for seeing if a key exists
	private boolean hasKey(String key)
	{
		String command = "SELECT COUNT(1) as count FROM " + tableName + " WHERE " + keyColumnName + " = ?";
		ResultSet set = driver.executeReturningStatement(JDBCStatementType.Select, command, new String[] { key });
		String out = resultAsString(set, "count");
		return out.charAt(0) == '1';
	}
	
	// Prototype for getting a key
	private String getKey(String key)
	{
		String command = "SELECT " + valueColumnName + " as searchedKey FROM " + tableName +" WHERE " + keyColumnName + " = ?";
		ResultSet set = driver.executeReturningStatement(JDBCStatementType.Select, command, new String[] { key });
		return resultAsString(set, "searchedKey");
	}
	
	// Prototype for creating a key
	private void createKey(String key, String value)
	{
		String command = "INSERT INTO " + tableName + " (GlobalKey, GlobalValue) VALUES (?, ?)";
		boolean status = driver.executeStatement(JDBCStatementType.Insert, command, new String[] { key, value });
		GlobalLog.log(LogFilter.Database, "CreateKey status: " + status);
	}
	
	// Get all keys containing a substring
	public List<String> getKeysWith(String keySubstring)
	{
		String command = "SELECT * FROM " + tableName + " WHERE " + keyColumnName + " like ?";
		ResultSet result = driver.executeReturningStatement(JDBCStatementType.Select, command, new String[] { "%" + keySubstring + "%" });
		
		List<String> keys = new ArrayList<String>();
		
		try
		{
			while(result.next())
			{
				String key = (String) result.getObject(1);
				keys.add(key);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return keys;
	}
	
	// Transforms a result into a string if possible.
	private String resultAsString(ResultSet rs, String key)
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
			GlobalLog.error(LogFilter.Database, e.toString());
			return null;
		}	
	}
}