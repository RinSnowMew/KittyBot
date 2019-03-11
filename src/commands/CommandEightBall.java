package commands;

import core.Command;
import dataStructures.*;

public class CommandEightBall extends Command
{
	String [] answers = {"It is certain.", "It is decidedly so.", "Without a doubt.", "Yes - definitely.", "You may rely on it.", "As I see it, yes.", "Most likely.",
			"Outlook good.", "Yes.", "Signs point to yes.", "Reply hazy, try again.", "Ask again later.", "Better not tell you now.", "Cannot predict now.",
			"Concentrate and ask again.", "Don't count on it.", "My reply is no.", "My sources say no.", "Outlook not so good.", "Very doubtful."};
	public CommandEightBall(KittyRole level, KittyRating rating) 
	{ 
		super(level, rating); 
	}
	
	@Override
	public String HelpText() { return "Answers a yes or no question! Warning: Kitty can not actually tell the future,"
			+ " she claims no responsibility for any lion mauling, lack of lottery wins, or felony charges."; }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call(answers[(int) (Math.random()*answers.length)]);
	}
}
