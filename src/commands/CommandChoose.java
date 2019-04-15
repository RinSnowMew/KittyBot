package commands;

import core.*;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandChoose extends Command
{
	public CommandChoose(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("ChooseInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String [] choices = input.args.split(",");
		if(choices.length == 1)
		{
			res.Call(LocStrings.Stub("ChooseOne"));
			return;
		}
		
		res.Call(String.format(LocStrings.Stub("ChooseChoice"), (choices[(int) (Math.random()*choices.length)]).toString()));
	}
}
