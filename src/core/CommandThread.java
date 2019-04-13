package core;

import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandThread  extends Thread
{
	// Pile of variables. This may be packaged later as thread arguments.
	CommandManager manager;
	UserInput input; 
	KittyGuild guild;
	KittyChannel channel;
	KittyUser user; 
	Response response;
	
	// Constructor
	public CommandThread(CommandManager manager, UserInput input, KittyGuild guild, KittyChannel channel, KittyUser user, Response response)
	{
		this.manager = manager;
		this.input = input;
		this.guild = guild;
		this.channel = channel;
		this.user = user;
		this.response = response;
	}
		
	// Method called when spawned as a thread
	@Override
	public void run() 
	{
		InvokeCommand();
	}

	private void InvokeCommand()
	{
		manager.Invoke(guild, channel, user, input, response);
	}
	
	// Handles the try catch requirement java has for sleeping
	@SuppressWarnings("unused")
	private void ThreadSleep(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
