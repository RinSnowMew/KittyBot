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
	public String HelpText() { return Localizer.Stub("With an input of x,y,z where x y and z are all choices, kitty will choose one"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String [] choices = input.args.split(",");
		if(choices.length == 1)
		{
			res.Call("I can't choose from *one* thing!");
			return;
		}
		
		res.Call(String.format(Localizer.Stub("I chooooooose %s!"), (choices[(int) (Math.random()*choices.length)]).toString()));
	}
}
