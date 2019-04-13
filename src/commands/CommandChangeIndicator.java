package commands;

import core.Command;
import core.Localizer;
import dataStructures.*;

public class CommandChangeIndicator extends Command
{
	public CommandChangeIndicator(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Changes the command indicator to any single character. By default, it's '!'. If more than one character is provided, the first one is used!"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String arg = input.args.trim();
		if(arg.length() == 0)
		{
			res.Call("Please specify a letter or symbol to use!");
			return;
		}
		
		guild.SetCommandIndicator(arg.substring(0, 1));
		res.Call(String.format(Localizer.Stub("Indicator changed to `%s`"), guild.GetCommandIndicator()));
	}
}
