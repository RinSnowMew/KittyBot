package commands;

import core.Command;
import core.Localizer;
import dataStructures.*;
import network.NetworkColiru;

public class CommandColiru extends Command
{
	NetworkColiru compiler = new NetworkColiru();
	
	public CommandColiru(KittyRole level, KittyRating rating) { super(level, rating);}
	
	@Override
	public String HelpText() { return Localizer.Stub("Will try to compile any c++ code you put in! Supports up to C++14 standard, uses g++."); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		res.Call(compiler.compileCPlus(input.args));
	}

}
