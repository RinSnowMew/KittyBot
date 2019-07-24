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

public class CommandRating extends Command
{
	public CommandRating(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("RatingInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
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
			res.send(LocStrings.stub("RatingChanged") + " " + newRating);
		else
			res.send(LocStrings.stub("RatingInvalid") + " `" + input.args + "`");
		
		if(newRating.equals("Filtered")) 
			res.send(LocStrings.stub("RatingWarning"));
	}
}