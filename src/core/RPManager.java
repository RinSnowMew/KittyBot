package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import dataStructures.*;
import utils.GlobalLog;
import utils.LogFilter;

public class RPManager 
{
	static HashMap <Long, KittyRP> logs = new HashMap<Long,KittyRP> ();
	public static RPManager instance = null;
	
	public RPManager() 
	{
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			GlobalLog.error(LogFilter.Core, "Attempted to create a second RP Manager!");
			return;
		}
	}
	
	public String newRP(KittyChannel channel, ArrayList <KittyUser> users)
	{
		if(logs.containsKey(Long.parseLong(channel.uniqueID)))
			return "You can't have 2 RP's running at the same time!";
		else
			logs.put(Long.parseLong(channel.uniqueID), new KittyRP(users, channel));
		return "RP started!";
	}
	
	public void addLine(KittyChannel channel, KittyUser user, UserInput input)
	{
		if(logs.containsKey(Long.parseLong(channel.uniqueID)) && !input.isValid())
				logs.get(Long.parseLong(channel.uniqueID)).addLine(user, input.message);
	}
	
	public File endRP(KittyChannel channel, KittyUser user) throws FileNotFoundException, UnsupportedEncodingException
	{
		if(!logs.containsKey(Long.parseLong(channel.uniqueID)))
		{
			return null; 
		}
		File log = logs.get(Long.parseLong(channel.uniqueID)).endRP(user);
		if(log != null)
			logs.remove(Long.parseLong(channel.uniqueID));
		return log;
	}

	public static void upkeep(KittyCore kitty) 
	{
		Response res = new Response(null, kitty);
		String reminder = "";
		ArrayList<Long> users;
		long currentTime = System.currentTimeMillis();
		for (Entry<Long, KittyRP> entry : logs.entrySet())
		{
			if(currentTime > entry.getValue().getTimer() + 1000 * 60 * 30)
			{
				reminder = "Don't forget about your RP log!";
				users = entry.getValue().getUsers();
				for(int i = 0; i < users.size(); i ++)
				{
						reminder += " <@" + users.get(i) + ">";
				}
				res.sendToChannel(reminder, entry.getValue().getChannel().uniqueID);
				reminder = "";
				
				entry.getValue().resetTimer();
			}
		}
	}
}
