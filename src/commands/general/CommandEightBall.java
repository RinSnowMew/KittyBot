package commands.general;

import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandEightBall extends Command
{
	String [] answers = 
	{ 
			  LocStrings.stub("EightBallYes1") 
			, LocStrings.stub("EightBallYes2")
			, LocStrings.stub("EightBallYes3")
			, LocStrings.stub("EightBallYes4")
			, LocStrings.stub("EightBallYes5")
			, LocStrings.stub("EightBallYes6")
			, LocStrings.stub("EightBallYes7")
			, LocStrings.stub("EightBallYes8")
			, LocStrings.stub("EightBallYes9")
			, LocStrings.stub("EightBallYes10")
			
			, LocStrings.stub("EightBallMaybe1")
			, LocStrings.stub("EightBallMaybe2")
			, LocStrings.stub("EightBallMaybe3")
			, LocStrings.stub("EightBallMaybe4")
			, LocStrings.stub("EightBallMaybe5")
			
			, LocStrings.stub("EightBallNo1")
			, LocStrings.stub("EightBallNo2")
			, LocStrings.stub("EightBallNo3")
			, LocStrings.stub("EightBallNo4")
			, LocStrings.stub("EightBallNo5")
	};
	
	public CommandEightBall(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("EightBallInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.args.trim().length() < 1)
			res.send(LocStrings.stub("EightBallError"));
		
		res.send(answers[(int) (Math.random()*answers.length)]);
	}
}
