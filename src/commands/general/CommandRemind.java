package commands.general;

import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandRemind extends Command
{
public CommandRemind(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("RemindInfo"); };
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		long time;
		try
		{
			time = Long.parseLong(input.args.split(" ")[0]);
		}
		catch(Exception e)
		{
			res.send("That's not a valid time");
			return;
		}
		
		res.send("Ok! I'll remind you!");
		
		try 
		{
			Thread.sleep(time * 1000 * 60);
			res.send("<@" + user.discordID + "> don't forget: " + input.args.substring(input.args.indexOf(" ")));
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}