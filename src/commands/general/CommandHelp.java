package commands.general;

import core.*;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

// Either allows for specifically looking up commands or gets a list of general commands to try out!
public class CommandHelp extends Command
{
	public CommandHelp(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("HelpInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{ 
		String help = Stats.instance.getHelpText(input.args);
		
		if(help == null)
		{
			help = LocStrings.stub("HelpDisplay");
		}
		
		res.send(help);
	}
	
}