package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandChangeIndicator extends Command
{
	public CommandChangeIndicator(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("ChangeIndicatorInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String arg = input.args.trim();
		if(arg.length() == 0)
		{
			res.Call(LocStrings.Stub("ChangeIndicatorError"));
			return;
		}
		
		guild.SetCommandIndicator(arg.substring(0, 1));
		res.Call(String.format(LocStrings.Stub("ChangeIndicatorChanged"), guild.GetCommandIndicator()));
	}
}
