package commands;

import core.Command;
import core.Localizer;
import dataStructures.*;

public class CommandBetBeans extends Command
{
	public CommandBetBeans(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return Localizer.Stub("Allows you to bet your beans, set your amount after the command! Warning! House always wins!\nSets of 3 will get 5x, 4 will get 10x, and 5 will get 1000x"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String call = "";
		int bet = 0;
		int win = 0; 
		try 
		{
			bet = Integer.parseInt(input.args);
			if(bet < 50)
			{
				res.Call(Localizer.Stub("Please bet with at least 50 beans!"));
				return;
			}
		}
		catch (NumberFormatException e)
		{
			res.Call(Localizer.Stub("That's not a valid bet!"));
			return;
		}
		
		if(user.GetBeans() < bet)
		{
			res.Call(Localizer.Stub("You don't have enough beans!"));
			return;
		}
		
		user.ChangeBeans(-bet);
		int [] slots = getSlots();
		call += slotString(slots);
		slots = sort(slots);
		
		win = getWinning(slots); 
		
		res.Call(call);
		
		if(win == 0)
			{
				res.Call(Localizer.Stub("Sorry, you didn't win, try again!"));
				return;
			}
		
		user.ChangeBeans(bet*win);
		res.Call(String.format(Localizer.Stub("You won %s beans!"), "" + (bet*win)));
	}
	
	private int getWinning(int [] slots)
	{
		int winning = 0; 
		int counter = 1;
		for(int j = 0; j < slots.length - 1; j++)
		{
			if(slots[j] == slots[j+1])
				counter++;
			else
				counter = 1;
			
			if(counter == 3)
				winning = 5;
			
			if(counter == 4)
				winning = 10;
			
			if(counter == 5)
				winning = 1000;
		}
		
		return winning;
	}
	
	private int[] getSlots() 
	{
		int[] nums = new int[5];
		for (int i = 0; i < nums.length; i++) 
		{
			nums[i] = (int) (Math.random() * 5) + 1;
		}
		return nums;
	}
	
	private int [] sort(int [] nums)
	{
		int [] sorted = new int [nums.length];
		
		int smallest= Integer.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < sorted.length; i++)
		{
			for(int j = 0; j < nums.length; j++)
			{
				if(nums[j] < smallest)
				{
					index = j; 
					smallest = nums[j];
				}
			}
			
			sorted[i] = smallest;
			smallest = Integer.MAX_VALUE;
			nums[index] = Integer.MAX_VALUE;
		}
		
		return sorted;
	}
	
	private String slotString(int [] slots)
	{
		String slotHearts = "";
		for(int i = 0; i < slots.length; i++)
		{
			switch(slots[i])
			{
			case 1:
				slotHearts += ":heart:";
				break;
			case 2:
				slotHearts += ":yellow_heart:";
				break;
			case 3:
				slotHearts += ":green_heart:";
				break;
			case 4:
				slotHearts += ":blue_heart:";
				break;
			case 5:
				slotHearts += ":purple_heart:";
				break;
			}
		}
		
		return slotHearts;
	}
}