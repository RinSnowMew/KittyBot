package core;

import java.util.HashMap;

import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyUser;
import utils.GlobalLog;

public class SubCommandFramework 
{
	HashMap<String, SubCommand> commands = new HashMap<String, SubCommand>();
	
	public SubCommandFramework()
	{
		
	}
	
	public void addCommand(String name, SubCommand command)
	{
		name = name.toLowerCase();
		if(commands.put(name, command) != null)
			GlobalLog.error("Multiple registration of a name with name '" + name + "'!");
		
		GlobalLog.log("Registered SubCommand " + name);
	}
	
	public SubCommandFormattable run(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		SubCommandFormattable res = null;
		try
		{
			res = commands.get(input.split(" ")[0].toLowerCase()).Invoke(guild, channel, user, input.substring(input.indexOf(" ")));
		}
		catch(Exception e)
		{
			res = commands.get(input.split(" ")[0].toLowerCase()).Invoke(guild, channel, user, "");
		}
		
		return res;
	}
}