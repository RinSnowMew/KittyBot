package commands;

import core.*;
import dataStructures.*;

public class CommandPollShow extends Command
{
	public CommandPollShow(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Will show the current poll running"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(!guild.polling)
		{
			res.Call(Localizer.Stub("There is no poll running!"));
			return;
		}
		String poll = String.format(Localizer.Stub("The current poll is `%s`\n"), guild.poll);
		poll += Localizer.Stub("And the choices are:\n");
		for(int i = 0; i < guild.choices.size(); i++)
		{
			poll += (i+1) + ": `" + guild.choices.get(i).choice + "`\n";
		}
		
		res.Call(poll);
	}
}
