package commands;

import core.Command;
import core.Localizer;
import dataStructures.*;

public class CommandBeansShow extends Command
{
	public CommandBeansShow(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Displays how many beans you have"); };
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call(String.format(Localizer.Stub("You have %s beans!"), user.GetBeans()));
	}
}
