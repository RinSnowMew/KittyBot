package commands;

import core.Command;
import core.Localizer;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandRating extends Command
{
	public CommandRating(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("'0' is fully sfw (Derpi and e621 searches are disabled), '1' is filtered (kitty auto appends a sfw tag on any searches), '2' is nsfw (any search will go through). Some other words are supported for setting filter as well."); }
	
	// Called when the command is run!
	@Override 
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String newRating = null; 
		switch(input.args.toLowerCase().trim())
		{
			case "0":
			case "sfw":
			case "safe":
			case "clean":
			case "work":
			case "safeforwork":
				guild.contentRating = KittyRating.Safe;
				newRating = "Safe";
				break;
			case "1":
			case "questionable":
			case "intermediate":
			case "middle":
			case "search filter":
			case "filter":
			case "filtered":
				guild.contentRating = KittyRating.Filtered;
				newRating = "Filtered";
				break;
			case "2":
			case "rude":
			case "nsfw":
			case "explicit":
			case "lewd":
			case "notsafeforwork":
				guild.contentRating = KittyRating.Explicit;
				newRating = "Explicit";
				break;
		}
		
		if(newRating != null)
			res.Call(Localizer.Stub("Kittybot content set to") + " " + newRating);
		else
			res.Call(Localizer.Stub("Invalid content rating") + " `" + input.args + "`");
		
		if(newRating.equals("Filtered")) 
			res.Call(Localizer.Stub("Warning: NSFW may slip through, images are only based on tags on their respective sites!"));
	}
}