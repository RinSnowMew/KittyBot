package dataStructures;

import java.util.Iterator;
import java.util.Vector;

import core.DatabaseTrackedObject;

public class KittyTrackedVector extends DatabaseTrackedObject
{
	// Variables
	private Vector<String> trackedVector = new Vector<String>();
	
	private final static String delimiter = "\n";
	private final static String split = "\\n";
	private final static String differentiator = "vector-";
	
	// Constructor - provide it with a unique ID as the second
	public KittyTrackedVector(String readableName, String UniqueID)
	{
		super(differentiator + readableName + UniqueID);
	}
	
	
	// See if the string is contined in the vector, regardless of case.
	public boolean containsIgnoreCase(String str)
	{
		Iterator<String> value = trackedVector.iterator();
		
		while (value.hasNext())
		{
			if(value.next().equalsIgnoreCase(str))
				return true;
		}
		
		return false;
	}
	
	// Mirrored behavior
	public boolean contains(String str)
	{
		return trackedVector.contains(str);
	}
	
	public void add(String str)
	{
		trackedVector.add(str);
		this.markDirty();
	}
	
	public boolean isEmpty()
	{
		return trackedVector.isEmpty();
	}
	
	public int size()
	{
		return trackedVector.size();
	}
	
	public String get(int index)
	{
		return trackedVector.get(index);
	}
	
	public void remove(String str)
	{
		trackedVector.remove(str);
		this.markDirty();
	}
	
	// Writes out the roles as a single string that can be parsed back in later
	@Override
	public String serialize() 
	{
		String toSerialize = "";
		
		for(int i = 0; i < trackedVector.size(); ++i)
		{
			if(i != 0)
				toSerialize += delimiter;
			
			toSerialize += trackedVector.get(i);
		}
		
		return toSerialize; 
	}

	// Reads in the roles as a delimited single string that needs parsing
	@Override
	public void deSerialzie(String string)
	{
		if(string != null)
		{
			if(string.length() >= 0)
			{
				String[] rolesSplit = string.split(split);
				
				for(int i = 0; i < rolesSplit.length; ++i)
					trackedVector.add(rolesSplit[i]);
			}
		}
	}
}