package commands.general;

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
	public String getHelpText() { return LocStrings.stub("RPEndInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
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
			res.sendFile(sending, "txt");
			res.send(LocStrings.stub("RPEndFileOut"));
		}
		else
		{
			res.send(LocStrings.stub("RPEndError"));
		}
	}
}
