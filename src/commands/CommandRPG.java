package commands;

import core.Command;
import core.Localizer;
import core.rpg.RPGFramework;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;


public class CommandRPG extends Command
{
	private RPGFramework framework;
	
	public CommandRPG(KittyRole role, KittyRating rating) 
	{ 
		super(role, rating);
		framework = new RPGFramework();
	}
	
	@Override
	public String HelpText() { return Localizer.Stub("Admin+ command only right now - experimental text RPG! Format is !rpg <rpg command>"); };
	
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.args == null || input.args.length() == 0)
		{
			res.Call(HelpText());
			return;
		}
		
		String result = null;
		synchronized(framework)
		{
			result = framework.Run(user.uniqueID, input.args.trim());
		}
		
		if(result == null)
		{
			res.Call(Localizer.Stub("Invalid RPG Command!"));
			return;
		}
		
		res.Call(result);
	}
}
