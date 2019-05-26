package commands;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

public class CommandDBStats extends Command
{
	DateFormat dateFormat;
	public CommandDBStats(KittyRole level, KittyRating rating)
	{
		super(level, rating);
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}
	
	@Override
	public String HelpText() { return LocStrings.Stub("DBStatsInfo"); }

	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		KittyEmbed embed = new KittyEmbed();
		embed.title = "Database Info";
		embed.descriptionText = "**Tracked Items:** " + DatabaseManager.instance.GetTrackedObjectsSize();
		embed.descriptionText += "\n";
		embed.descriptionText += "**Last Upkeep:** " + dateFormat.format(DatabaseManager.instance.GetLastUpkeep()) + " UTC-7"; 
		embed.color = new Color(7*16, 8*16, 9*16);
		
		res.CallEmbed(embed);
	}
}
