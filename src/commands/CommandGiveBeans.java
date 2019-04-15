package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandGiveBeans extends Command
{
	public CommandGiveBeans (KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("GiveBeansInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		int beans = 0;
		try {
			beans = Integer.parseInt(input.args.split(" ")[0]);
		}
		catch (NumberFormatException e)
		{
			res.Call(LocStrings.Stub("GiveBeansInvalid"));
			return;
		}
		
		if(input.mentions == null)
		{
			res.Call(LocStrings.Stub("GiveBeansNoneMentioned"));
			return;
		}
		
		for(int i = 0; i < input.mentions.length; i++)
		{
			input.mentions[i].ChangeBeans(beans);
			res.Call(String.format(LocStrings.Stub("GiveBeansSuccess"), input.mentions[i].name, "" + beans));
		}
	}
}
