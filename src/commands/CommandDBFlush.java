package commands;

import java.awt.Color;

import core.Command;
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
	public String HelpText() { return LocStrings.Stub("DBFlushInfo"); }

	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		int numUpdated = DatabaseManager.instance.Upkeep();

		KittyEmbed embed = new KittyEmbed();
		embed.title = "Database queue flushed";
		embed.descriptionText = "**Dirty:** " + numUpdated;
		embed.color = new Color(7*16, 8*16, 9*16);
		
		res.CallEmbed(embed);
	}
}
