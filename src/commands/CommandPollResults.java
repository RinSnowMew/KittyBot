package commands;

import java.util.ArrayList;

import core.*;
import dataStructures.*;

public class CommandPollResults extends Command
{
	public CommandPollResults(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Will show current results of a poll and percentages of votes per choice"); }
	
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
		
		for(int i = 0; i < votes.size(); i++)
		{
			results += String.format(Localizer.Stub("%s people voted for %s `%s` with `%s%` !\n "), votes.get(i).votes, votes.get(i).choice, (int)(((double)votes.get(i).votes) / ((double)totalVotes) * 100));
		}
		
		res.Call(results);
	}
}
