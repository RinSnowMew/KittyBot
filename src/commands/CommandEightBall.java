package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandEightBall extends Command
{
	String [] answers = 
	{ 
			  LocStrings.Stub("EightBallYes1") 
			, LocStrings.Stub("EightBallYes2")
			, LocStrings.Stub("EightBallYes3")
			, LocStrings.Stub("EightBallYes4")
			, LocStrings.Stub("EightBallYes5")
			, LocStrings.Stub("EightBallYes6")
			, LocStrings.Stub("EightBallYes7")
			, LocStrings.Stub("EightBallYes8")
			, LocStrings.Stub("EightBallYes9")
			, LocStrings.Stub("EightBallYes10")
			
			, LocStrings.Stub("EightBallMaybe1")
			, LocStrings.Stub("EightBallMaybe2")
			, LocStrings.Stub("EightBallMaybe3")
			, LocStrings.Stub("EightBallMaybe4")
			, LocStrings.Stub("EightBallMaybe5")
			
			, LocStrings.Stub("EightBallNo1")
			, LocStrings.Stub("EightBallNo2")
			, LocStrings.Stub("EightBallNo3")
			, LocStrings.Stub("EightBallNo4")
			, LocStrings.Stub("EightBallNo5")
	};
	
	public CommandEightBall(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("EightBallInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.args.trim().length() < 1)
			res.Call(LocStrings.Stub("EightBallError"));
		
		res.Call(answers[(int) (Math.random()*answers.length)]);
	}
}
