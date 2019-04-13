package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import core.Command;
import core.Localizer;
import core.RPManager;
import dataStructures.*;

public class CommandRPEnd extends Command
{
	public CommandRPEnd (KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Ends RP in channel"); }
	
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
			res.Call(Localizer.Stub("Here's your file!"));
		}
		else
		{
			res.Call(Localizer.Stub("You can't end this RP!"));
		}
	}
}
