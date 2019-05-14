package commands;

import core.Command;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandTradeBeans extends Command
{
	public CommandTradeBeans(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("TradeBeansInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.mentions == null)
		{
			res.Call(LocStrings.Stub("TradeBeansNoTargetError"));
			return;
		}
		
		int beans = 0;
		try {
			beans = Integer.parseInt(input.args.split(" ")[0]);
		}
		catch (NumberFormatException e)
		{
			res.Call(LocStrings.Stub("TradeBeansIntParseError"));
			return;
		}
		
		if(user.GetBeans() < beans)
		{
			res.Call(LocStrings.Stub("TradeBeansNotEnoughError"));
			return;
		}
		
		if(beans < 0)
		{
			res.Call(String.format(LocStrings.Stub("TradeBeansStealingBeans"), user.name));
			user.ChangeBeans(-10);
			return; 
		}
		
		input.mentions[0].ChangeBeans(beans);
		user.ChangeBeans(-beans);
		res.Call(String.format(LocStrings.Stub("TradeBeansSuccess"), user.name, input.mentions[0].name, beans));
	}
}
