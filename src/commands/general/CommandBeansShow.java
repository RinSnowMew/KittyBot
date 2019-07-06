package commands.general;

import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandBeansShow extends Command
{
	public CommandBeansShow(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("BeansShowInfo"); };
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.mentions == null)
			res.send(String.format(LocStrings.stub("BeansShowDisplay"), user.getBeans()));
		else
		{
			String mentionedBeans = ""; 
			for(KittyUser mentioned:input.mentions)
			{
				mentionedBeans += mentioned.name + " has " + mentioned.getBeans() + " ";
			}
			res.send(String.format(LocStrings.stub("BeansShowMentioned"), mentionedBeans));
		}
	}
}
