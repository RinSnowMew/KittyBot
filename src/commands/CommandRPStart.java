package commands;

import java.util.ArrayList;

import core.Command;
import core.LocStrings;
import core.RPManager;
import dataStructures.*;

public class CommandRPStart extends Command
{
	public CommandRPStart (KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("RPStartInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		ArrayList <KittyUser> users = new ArrayList<KittyUser>(); 
		users.add(user);
		if(input.mentions != null)
		{
			for(int i = 0; i < input.mentions.length; i++)
			{
				users.add(input.mentions[i]);
			}
		}
		
		res.Call(RPManager.instance.newRP(channel, users));
	}
}
