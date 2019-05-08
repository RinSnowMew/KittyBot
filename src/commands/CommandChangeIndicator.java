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
		
		String indicator = arg.substring(0, 1);
		if(indicator.charAt(0) == '\n' || indicator.charAt(0) == '\t' || indicator.charAt(0) == '\r')
		{
			res.Call(LocStrings.Lookup("ChangeIndicatorError"));
			return;
		}
		
		guild.SetCommandIndicator(indicator);
		res.Call(String.format(LocStrings.Stub("ChangeIndicatorChanged"), guild.GetCommandIndicator()));
	}
}
