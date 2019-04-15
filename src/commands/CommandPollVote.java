package commands;

import core.*;
import dataStructures.*;

public class CommandPollVote extends Command
{
	public CommandPollVote(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("PollVoteInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(guild.polling)
		{
			if(guild.hasVoted.contains(user.uniqueID))
			{
				res.Call(LocStrings.Stub("PollVoteAlreadyVoted"));
				return;
			}
			try 
			{
				int voteNum = Integer.parseInt(input.args)-1;
				if(voteNum >= guild.choices.size() || voteNum < 0)
				{
					res.Call(String.format(LocStrings.Stub("PollVoteNotValidVote"), voteNum));
					return;
				}
				
				KittyPoll polled = guild.choices.get(voteNum);
				polled.votes++;
				guild.hasVoted.add(user.uniqueID);
				res.Call(LocStrings.Stub("PollVoteSuccess") + " `" + polled.choice + "`!");
				return;
			}
			catch (NumberFormatException e)
			{
				res.Call(LocStrings.Stub("PollVoteNotValidNumber"));
				return;
			}
		}
		
		res.Call(LocStrings.Stub("PollVoteNoPoll"));
	}
}
