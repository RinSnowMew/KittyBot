package commands.poll;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.UserInput;

public class SubCommandShow extends SubCommand
{
	public SubCommandShow(KittyRole level, KittyRating rating) { super(level, rating); }
	
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		if(!guild.polling)
		{
			return new SubCommandFormattable (LocStrings.stub("PollShowNoPoll"));
		}
		String poll = String.format(LocStrings.stub("PollShowPoll"), guild.poll);
		poll += LocStrings.stub("PollShowChoices");
		for(int i = 0; i < guild.choices.size(); i++)
		{
			poll += (i+1) + ": `" + guild.choices.get(i).choice + "`\n";
		}
		
		return new SubCommandFormattable (poll);
	}
}
