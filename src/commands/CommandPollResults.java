package commands;

import java.util.ArrayList;

import core.*;
import dataStructures.*;

public class CommandPollResults extends Command
{
	public CommandPollResults(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("PollResultsInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
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
			results += String.format(LocStrings.Stub("PollResultsResponse"), votes.get(i).votes, votes.get(i).choice, (int)(((double)votes.get(i).votes) / ((double)totalVotes) * 100));
		}
		
		res.Call(results);
	}
}
