package commands;

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
		
		public void ApplyBoop()
		{
			++boops;
			MarkDirty();
		}

		public int HowMany()
		{
			return boops;
		}
		
		@Override
		public String Serialize() { return "" + boops; }

		@Override
		public void DeSerialzie(String string) 
		{ 
			try
			{
				boops = Integer.parseInt(string);
			}
			catch (NumberFormatException e)
			{
				GlobalLog.Warn(LogFilter.Command, "No valid value was found for boops! Starting over at 0!");
				boops = 0;
				MarkDirty();
			}
		}
	}
	
	public static BoopTracker boopTracker;
	
	// Constructor
	public CommandBoop(KittyRole level, KittyRating rating) 
	{ 
		super(level, rating);
		boopTracker = new BoopTracker();
		DatabaseManager.instance.Register(boopTracker);
	}

	@Override
	public String HelpText() { return Localizer.Stub("Kitty will react with a counter"); }
	
	// Called when the command is run!
	@Override 
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		if(input.mentions == null)
		{
			boopTracker.ApplyBoop();
			res.Call(String.format(Localizer.Stub("Woah! %s booped me! That's %s total!"), user.name, boopTracker.HowMany()));
		}
		else
		{
			if(input.mentions.length == 1)
			{
				boopTracker.ApplyBoop();
				res.Call(String.format(Localizer.Stub("%s booped %s!"), user.name, input.mentions[0].name));
				return;
			}
			
			String booped = ""; 
			for(int i = 0; i < input.mentions.length; i++)
			{
				boopTracker.ApplyBoop();
				if(i < input.mentions.length-1)
					booped += input.mentions[i].name + ", ";
				else
					booped +=  "and " + input.mentions[i].name;
			}
			
			res.Call(String.format(Localizer.Stub("%s booped several others - %s!"), user.name, booped));
		}
	}
}