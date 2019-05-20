package network;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utils.GlobalLog;
import utils.LogFilter;

// Only to be called by the generic driver.
public class JDBCDriverSQLite extends JDBCDriver
{
	Connection connection = null;
	String databaseFolder = "db/";
	String databaseName = "catfood";
	
	@Override
	public boolean Connect()
	{		
		try 
		{
			{ // Scope to discard file...
				File f = new File(databaseFolder);
				if(!f.exists() || !f.isDirectory())
				{
					f.mkdir();
				}
			}
			
			// db parameters
			String url = "jdbc:sqlite:db/" + databaseName + ".db";
			
			// create a connection to the database
			connection = DriverManager.getConnection(url);
			
			GlobalLog.Log(LogFilter.Database, "Connection to SQLite has been established.");
		} 
		catch (SQLException e) 
		{
			GlobalLog.Error(e.getMessage());
		}
		
		return connection != null;
	}
	
	@Override
	public boolean Disconnect()
	{
		try
		{
			if (connection != null)
				connection.close();
			
			return true;
		} 
		catch (SQLException ex) 
		{
			GlobalLog.Error(ex.getMessage());
			return false;
		}
	}
	
	@Override
	public ResultSet ExecuteReturningStatement(String command, String[] args)
	{
		if(connection == null)
			return null;
		
		if(command == null || command.length() == 0)
			return null;
		
		try 
		{
			if(args != null && args.length > 0)
			{
				PreparedStatement statement = connection.prepareStatement(command);
				
				for(int i = 0; i < args.length; ++i)
					statement.setString(i + 1, args[i]);
				
				ResultSet set = statement.executeQuery();
				return set;
			}
			else
			{
				Statement statement = connection.createStatement();
				ResultSet set = statement.executeQuery(command);
				return set;
			}
		}
		catch (SQLException e) 
		{
			try
			{
				GlobalLog.Fatal(e.getMessage());
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			
			return null;
		}
	}
	
	public boolean ExecuteStatement(String command, String[] args)
	{
		if(connection == null)
			return false;
		
		if(command == null || command.length() == 0)
			return false;
		
		try 
		{
			if(args != null && args.length > 0)
			{
				PreparedStatement statement = connection.prepareStatement(command);
				
				for(int i = 0; i < args.length; ++i)
					statement.setString(i + 1, args[i]);
				
				boolean executed = statement.execute();
				return executed;
			}
			else
			{
				Statement statement = connection.createStatement();
				boolean executed = statement.execute(command);
				return executed;
			}
		}
		catch (SQLException e) 
		{
			try
			{
				GlobalLog.Fatal(e.getMessage());
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			
			return false;
		}
	}
}
