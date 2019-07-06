package commands.general;

import core.Command;
import core.LocStrings;
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
	public String getHelpText() { return LocStrings.stub("RPGInfo"); };
	
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.args == null || input.args.length() == 0)
		{
			String output = getHelpText();
			res.send(output);
			return;
		}
		
		String result = null;
		synchronized(framework)
		{
			result = framework.run(user.uniqueID, input.args.trim());
		}
		
		if(result == null)
		{
			res.send(LocStrings.stub("RPGInvalid"));
			return;
		}
		
		res.send(result);
	}
}
