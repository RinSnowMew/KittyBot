package core;

import java.util.HashMap;

import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyUser;
import dataStructures.UserInput;
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
	
	public SubCommandFormattable run(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		SubCommand res = commands.get(input.args.split(" ")[0].toLowerCase());
		try 
		{
			input.args = input.args.substring(input.args.indexOf(' ')).trim();
		}
		catch(Exception e){}
		
		try 
		{
			return res.Invoke(guild, channel, user, input);
		}
		catch(Exception e)
		{
			String help = Stats.instance.getHelpText(input.key);
			if(help != null)
				return new SubCommandFormattable(help);
			return new SubCommandFormattable("Something has gone very wrong.");
		}
	}
}