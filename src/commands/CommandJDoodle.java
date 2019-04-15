package commands;

import core.Command;
import core.LocStrings;
import dataStructures.*;
import network.NetworkJDoodle;

public class CommandJDoodle extends Command 
{
	NetworkJDoodle compiler = new NetworkJDoodle();
	
	public CommandJDoodle(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("JDoodleInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.args.trim().length() < 1)
		{
			res.Call(LocStrings.Stub("JDoodleError"));
			return;
		}
		
		res.Call(compiler.compileJava(input.args));
	}
}
