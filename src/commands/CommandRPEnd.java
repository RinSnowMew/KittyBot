package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import core.Command;
import core.LocStrings;
import core.RPManager;
import dataStructures.*;

public class CommandRPEnd extends Command
{
	public CommandRPEnd (KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("RPEndInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		File sending = null;
		
		try 
		{
			sending = RPManager.instance.endRP(channel, user);
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e) 
		{
			System.out.println("I don't know how you got here");
		}
		
		if(sending != null)
		{
			res.CallFile(sending, "txt");
			res.Call(LocStrings.Stub("RPEndFileOut"));
		}
		else
		{
			res.Call(LocStrings.Stub("RPEndError"));
		}
	}
}
