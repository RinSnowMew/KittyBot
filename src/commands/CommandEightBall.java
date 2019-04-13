package commands;

import core.Command;
import core.Localizer;
import dataStructures.*;

public class CommandEightBall extends Command
{
	String [] answers = 
	{ 
			  Localizer.Stub("It is certain.") 
			, Localizer.Stub("It is decidedly so.")
			, Localizer.Stub("Without a doubt.")
			, Localizer.Stub("Yes - definitely.")
			, Localizer.Stub("You may rely on it.")
			, Localizer.Stub("As I see it, yes.")
			, Localizer.Stub("Most likely.")
			, Localizer.Stub("Outlook good.")
			, Localizer.Stub("Yes.")
			, Localizer.Stub("Signs point to yes.")
			
			, Localizer.Stub("Reply hazy, try again.")
			, Localizer.Stub("Ask again later.")
			, Localizer.Stub("Better not tell you now.")
			, Localizer.Stub("Cannot predict now.")
			, Localizer.Stub("Concentrate and ask again.")
			
			, Localizer.Stub("Don't count on it.")
			, Localizer.Stub("My reply is no.")
			, Localizer.Stub("My sources say no.")
			, Localizer.Stub("Outlook not so good.")
			, Localizer.Stub("Very doubtful.")
	};
	
	public CommandEightBall(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Answers a yes or no question! Warning: Kitty can not actually tell the future, she claims no responsibility for any lion mauling, lack of lottery wins, or felony charges."); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call(answers[(int) (Math.random()*answers.length)]);
	}
}
