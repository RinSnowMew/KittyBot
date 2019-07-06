package commands.general;

import core.*;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;
import utils.GlobalLog;
import utils.LogFilter;

public class CommandBoop extends Command
{
	public class BoopTracker extends DatabaseTrackedObject
	{
		private int boops = 0;
		
		public BoopTracker() { super("booptracker"); }
		
		public void applyBoop()
		{
			++boops;
			markDirty();
		}

		public int howMany()
		{
			return boops;
		}
		
		@Override
		public String serialize() { return "" + boops; }

		@Override
		public void deSerialzie(String string) 
		{ 
			try
			{
				boops = Integer.parseInt(string);
			}
			catch (NumberFormatException e)
			{
				GlobalLog.warn(LogFilter.Command, "No valid value was found for boops! Starting over at 0!");
				boops = 0;
				markDirty();
			}
		}
	}
	
	public static BoopTracker boopTracker;
	
	// Constructor
	public CommandBoop(KittyRole level, KittyRating rating) 
	{ 
		super(level, rating);
		boopTracker = new BoopTracker();
		DatabaseManager.instance.globalRegister(boopTracker);
	}

	@Override
	public String getHelpText() { return LocStrings.stub("BoopInfo"); }
	
	// Called when the command is run!
	@Override 
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.mentions == null)
		{
			boopTracker.applyBoop();
			res.send(String.format(LocStrings.stub("BoopStandard"), user.name, boopTracker.howMany()));
		}
		else
		{
			if(input.mentions.length == 1)
			{
				boopTracker.applyBoop();
				res.send(String.format(LocStrings.stub("BoopPerson"), user.name, input.mentions[0].name));
				return;
			}
			
			String booped = ""; 
			for(int i = 0; i < input.mentions.length; i++)
			{
				boopTracker.applyBoop();
				if(i < input.mentions.length-1)
					booped += input.mentions[i].name + ", ";
				else
					booped +=  "and " + input.mentions[i].name;
			}
			
			res.send(String.format(LocStrings.stub("BoopMultiple"), user.name, booped));
		}
	}
}