package commands.general;

import core.Command;
import core.Constants;
import core.DatabaseManager;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyEmbed;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandDBFlush extends Command
{
	public CommandDBFlush(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("DBFlushInfo"); }

	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		int numUpdated = DatabaseManager.instance.upkeep();

		KittyEmbed embed = new KittyEmbed();
		embed.title = "Database queue flushed";
		embed.descriptionText = "**Dirty:** " + numUpdated;
		embed.color = Constants.ColorDefault;
		
		res.send(embed);
	}
}
