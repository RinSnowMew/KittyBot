package commands.poll;

import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyPoll;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;

public class SubCommandVote extends SubCommand
{
	public SubCommandVote(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		if(guild.polling)
		{
			if(guild.hasVoted.contains(user.uniqueID))
			{
				return new SubCommandFormattable (LocStrings.stub("PollVoteAlreadyVoted"));
			}
			try 
			{
				int voteNum = Integer.parseInt(input.substring(input.indexOf(" ")).trim())-1;
				if(voteNum >= guild.choices.size() || voteNum < 0)
				{
					return new SubCommandFormattable (String.format(LocStrings.stub("PollVoteNotValidVote"), voteNum));
				}
				
				KittyPoll polled = guild.choices.get(voteNum);
				polled.votes++;
				guild.hasVoted.add(user.uniqueID);
				return new SubCommandFormattable (LocStrings.stub("PollVoteSuccess") + " `" + polled.choice + "`!");
			}
			catch (NumberFormatException e)
			{
				return new SubCommandFormattable (LocStrings.stub("PollVoteNotValidNumber"));
			}
		}
		
		return new SubCommandFormattable (LocStrings.stub("PollVoteNoPoll"));
	}
}
