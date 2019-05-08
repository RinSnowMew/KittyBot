package dataStructures;

import java.util.Vector;

import core.DatabaseTrackedObject;

// Acts as a proxy of sorts for the allowedRole arraylist, keeping it tracked for a specific guild.
@SuppressWarnings("unused") 
public class KittyTrackedVector extends DatabaseTrackedObject
{
	// Variables
	private Vector<String> allowedRole = new Vector<String>();
	private final String databaseID; // in-database ID of this value
	
	private final static String delimiter = "\n";
	private final static String split = "\\n";
	private final static String differentiator = "vector-";
	
	// Constructor - provide it with a unique ID as the second
	public KittyTrackedVector(String readableName, String UniqueID)
	{
		super(differentiator + readableName + UniqueID);
		databaseID = differentiator + readableName + UniqueID;
	}
	
	// Mirrored behavior
	public boolean contains(String role)
	{
		return allowedRole.contains(role);
	}
	
	public void add(String role)
	{
		allowedRole.add(role);
		this.MarkDirty();
	}
	
	public boolean isEmpty()
	{
		return allowedRole.isEmpty();
	}
	
	public int size()
	{
		return allowedRole.size();
	}
	
	public String get(int index)
	{
		return allowedRole.get(index);
	}
	
	public void remove(String role)
	{
		allowedRole.remove(role);
		this.MarkDirty();
	}
	
	// Writes out the roles as a single string that can be parsed back in later
	@Override
	public String Serialize() 
	{
		String toSerialize = "";
		
		for(int i = 0; i < allowedRole.size(); ++i)
		{
			if(i != 0)
				toSerialize += delimiter;
			
			toSerialize += allowedRole.get(i);
		}
		
		return toSerialize; 
	}

	// Reads in the roles as a delimited single string that needs parsing
	@Override
	public void DeSerialzie(String string)
	{
		if(string != null)
		{
			if(string.length() >= 0)
			{
				String[] rolesSplit = string.split(split);
				
				for(int i = 0; i < rolesSplit.length; ++i)
					allowedRole.add(rolesSplit[i]);
			}
		}
	}
}