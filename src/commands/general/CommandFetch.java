package commands.general;

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
	public String getHelpText() { return LocStrings.stub("FetchInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String emote = input.args.split(" ")[0];
		if(emote.startsWith("<") && emote.endsWith(">"))
		{
			int num = (int)(Math.random() * 6) + 1;
			switch(num)
			{
				case 1:
					res.send(String.format(LocStrings.stub("FetchBringBack"), emote));
					break;
				case 2:
					res.send(String.format(LocStrings.stub("FetchRunAway")));
					break;
				case 3:
					res.send(String.format(LocStrings.stub("FetchBringBackWrong"), guild.emoji.get((int)(Math.random() * guild.emoji.size()) + 1)));
					break;
				case 4: 
					res.send(String.format(LocStrings.stub("FetchEat"), emote));
					break;
				case 5: 
					res.send(String.format(LocStrings.stub("FetchCatchRun"), emote));
					break;
				case 6:
					res.send(String.format(LocStrings.stub("FetchStare"), user.name));
					break;
					default:
						res.send("" + num);
			}
		}
		else
		{
			res.send(String.format(LocStrings.stub("FetchError")));
			System.out.println(emote);
		}
	}
}