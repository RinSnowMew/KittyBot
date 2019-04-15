package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;
import network.NetworkColiru;

public class CommandColiru extends Command
{
	NetworkColiru compiler = new NetworkColiru();
	
	public CommandColiru(KittyRole level, KittyRating rating) { super(level, rating);}
	
	@Override
	public String HelpText() { return LocStrings.Stub("ColiruInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.args.trim().length() < 1)
		{
			res.Call(LocStrings.Stub("ColiruError"));
			return;
		}
		
		res.Call(compiler.compileCPlus(input.args));
	}

}
