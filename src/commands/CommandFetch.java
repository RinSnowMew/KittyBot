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

public class CommandFetch extends Command
{
	public CommandFetch(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("FetchInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String emote = input.args.split(" ")[0];
		if(emote.startsWith("<") && emote.endsWith(">"))
		{
			int num = (int)(Math.random() * 6) + 1;
			switch(num)
			{
				case 1:
					res.Call(String.format(LocStrings.Stub("FetchBringBack"), emote));
					break;
				case 2:
					res.Call(String.format(LocStrings.Stub("FetchRunAway")));
					break;
				case 3:
					res.Call(String.format(LocStrings.Stub("FetchBringBackWrong"), guild.emoji.get((int)(Math.random() * guild.emoji.size()) + 1)));
					break;
				case 4: 
					res.Call(String.format(LocStrings.Stub("FetchEat"), emote));
					break;
				case 5: 
					res.Call(String.format(LocStrings.Stub("FetchCatchRun"), emote));
					break;
				case 6:
					res.Call(String.format(LocStrings.Stub("FetchStare"), user.name));
					break;
					default:
						res.Call("" + num);
			}
		}
		else
		{
			res.Call(String.format(LocStrings.Stub("FetchError")));
			System.out.println(emote);
		}
	}
}