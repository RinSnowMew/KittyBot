package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandBeansShow extends Command
{
	public CommandBeansShow(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("BeansShowInfo"); };
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call(String.format(LocStrings.Stub("BeansShowDisplay"), user.GetBeans()));
	}
}
