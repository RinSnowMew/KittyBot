package commands.general;

import core.*;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandDoWork extends Command
{
	// Required constructor
	public CommandDoWork(KittyRole level, KittyRating rating) { super(level, rating); }

	@Override
	public String getHelpText() { return LocStrings.stub("DoWorkInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		@SuppressWarnings("unused")
		long thispieceofshit = 0; 
		for(int i = 0; i < 200000000 + Math.random() * 30; i ++)
		{
			thispieceofshit += Math.log((double)i);
		}
		
		res.send(LocStrings.stub("DoWorkFinished"));
	}
}