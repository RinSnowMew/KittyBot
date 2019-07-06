package commands.general;

import java.io.File;
import java.io.IOException;
import core.Command;
import core.LocStrings;
import dataStructures.*;
import network.*;
import utils.ImageUtils;

public class CommandWolfram extends Command 
{
	NetworkWolfram searcher = new NetworkWolfram();
	
	public CommandWolfram(KittyRole level, KittyRating rating) { super(level, rating);}
	
	@Override
	public String getHelpText() { return LocStrings.stub("WolframInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.args == null || input.args.trim().length() == 0)
			res.send(LocStrings.stub("WolframNoArgs"));
		
		try 
		{
			File pic = new File(searcher.getWolfram(input.args));
			res.sendFile(pic, "png");
			ImageUtils.blockingFileDelete(pic);
		} 
		catch (IOException e)
		{
			res.send(LocStrings.stub("WolframError"));
		}
	}
}