package commands.general;

import java.util.ArrayList;
import java.util.List;

import core.Command;
import core.Constants;
import core.DatabaseManager;
import core.LocStrings;
import core.ObjectBuilderFactory;
import dataStructures.KittyChannel;
import dataStructures.KittyEmbed;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Pair;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.GlobalLog;
import utils.LogFilter;

public class CommandLeaderboard extends Command
{
	public CommandLeaderboard(KittyRole roleLevel, KittyRating contentRating) { super(roleLevel, contentRating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("LeaderboardInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		// Start by force-flushing. We need to be up-to-date.
		GlobalLog.log("Flushed database for " + DatabaseManager.instance.upkeep() + " items.");
		
		// Get all users associated with a guild and get their bean count
		String guildID = guild.uniqueID;
		List<Pair<Long, String>> users = new ArrayList<Pair<Long, String>>();
		List<String> out = DatabaseManager.instance.scrapeGlobalForString(guildID);
		
		for(String item : out)
		{
			String val = item.replace("" + guildID, "");
			if(!val.contains("-") && val.length() > 0)
			{
				String userID = val;
				
				if(userID.length() < 1)
					continue;
				
				// Look up in the database the user beans
				String dbUserData = DatabaseManager.instance.globalGetRemoteValue(guildID + userID);
				
				// Leverage the fact that the user is automatically read and parsed when constructed.
				Long parsedBeans = KittyUser.parseBeans(KittyUser.prepareFromString(dbUserData));
				
				// If we were only using cached users, we would use this. However, we're not.
				// KittyUser cachedUser = ObjectBuilderFactory.getCachedUser(guildID, userID);
				users.add(new Pair<Long, String>(parsedBeans, userID));
			}
		}
		
		// Sort users by beans
		users.sort((pair1, pair2) -> {
			long difference = pair2.First - pair1.First;
			
			if(difference < Integer.MIN_VALUE)
				return Integer.MIN_VALUE; 
			
			if(difference > Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
			
			return (int)difference;
		});
		
		GlobalLog.log(LogFilter.Command, "Sorted through " + users.size() + " KittyUsers for leaderboard purposes.");
		
		// Configure output
		final int listSize = 10;
		KittyEmbed embed = new KittyEmbed();
		embed.color = Constants.ColorDefault;
		embed.title = LocStrings.stub("LeaderboardTitle");
		embed.descriptionText = "";
		
		for(int i = 0; i < listSize && i < users.size(); ++i)
		{
			// Only now that we've sorted do we do the full construction and caching of users
			Pair<Long, String> userPair = users.get(i);
			KittyUser cachedUser = ObjectBuilderFactory.getKittyUser(guildID, userPair.Second);
			
			embed.descriptionText += "**" + (i + 1) + ":** " + userPair.First + " - " + cachedUser.name;
			embed.descriptionText += "\n";
		}
		
		// Write out embed result
		res.send(embed);
	}
}
