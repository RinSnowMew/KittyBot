package commands.poll;

import java.util.ArrayList;

import core.*;
import dataStructures.*;

public class SubCommandResults extends SubCommand
{
	public SubCommandResults(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		String results = "";
		int totalVotes = 0; 
		ArrayList<KittyPoll> votes = guild.choices;
		
		for(int i = 0; i < votes.size(); i++)
		{
			totalVotes += votes.get(i).votes;
		}
		
		results += "The poll was " + guild.poll + "\n";
		
		for(int i = 0; i < votes.size(); i++)
		{
			results += String.format(LocStrings.stub("PollResultsResponse"), votes.get(i).votes, votes.get(i).choice, (int)(((double)votes.get(i).votes) / ((double)totalVotes) * 100)) + "\n";
		}
		
		return new SubCommandFormattable (results);
	}
}
