package dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import core.DatabaseManager;
import core.DatabaseTrackedObject;
import utils.AdminControl;

// Context for a given guild for kittybot. Primarily designed to hold guild-specific settings.
public class KittyGuild extends DatabaseTrackedObject
{
	// Variables
	public final String uniqueID;
	public KittyRating contentRating;
	public KittyUser guildOwner;
	public boolean polling;
	public String poll; 
	public boolean raffling; 
	public int raffleCost; 
	public ArrayList<String> hasVoted = new ArrayList<String>();
	public ArrayList<String> emoji = new ArrayList<String>();
	public ArrayList <KittyPoll> choices = new ArrayList<KittyPoll>();
	public AdminControl control;
	public HashMap <KittyUser, Boolean> raffleUsers = new HashMap<KittyUser, Boolean>();
	
	// Database synced info
	private final String roleListName = "guildRoles";
	public final KittyTrackedVector roleList;
	
	private final String beansName = "guildBeans";
	public final KittyTrackedLong beans;
	
	private String commandIndicator;
	
	private void RegisterTrackedObjects()
	{
		DatabaseManager.instance.Register(roleList);
		DatabaseManager.instance.Register(beans);
	}
	
	// Default content for a guild
	public KittyGuild(String uniqueID, AdminControl adminControl, ArrayList <String> emoji)
	{
		super(uniqueID);
		this.uniqueID = uniqueID;
		roleList = new KittyTrackedVector(roleListName, uniqueID);
		beans = new KittyTrackedLong(beansName, uniqueID);
		RegisterTrackedObjects();
		
		control = adminControl;
		this.contentRating = KittyRating.Safe; 
		this.polling = false;
		this.emoji = emoji;
		SetCommandIndicator("!");
	}
	
	// Explicit constructor
	public KittyGuild(String commandIndicator, KittyRating contentRating, KittyUser guildOwner, String uniqueID)
	{
		super(uniqueID);
		this.uniqueID = uniqueID;
		roleList = new KittyTrackedVector(roleListName, uniqueID);
		beans = new KittyTrackedLong(beansName, uniqueID);
		RegisterTrackedObjects();
		
		SetCommandIndicator(commandIndicator);
		this.contentRating = contentRating;
		this.polling = false;
		this.guildOwner = guildOwner;
	}
	
	public String startPoll(String poll)
	{
		polling = true; 
		this.poll = poll;
		return "Poll `" + poll + "` started! Don't forget to add choices with `!poll choice`~";
	}
	
	public String addChoiceToPoll(String choice)
	{
		choices.add(new KittyPoll(choice));
		return "Added `" + choice + "` to poll!";
	}
	
	public String endPoll()
	{
		choices.clear();
		hasVoted.clear();
		this.polling = false;
		poll = null;
		return "Poll ended!";
	}
	
	//Raffle methods
	public boolean startRaffle(int beanCost)
	{
		if(raffling)
		{
			return false;
		}
		raffleCost = beanCost; 
		raffling = true; 
		return true;
	}
	
	public boolean endRaffle()
	{
		if(raffling)
		{
			raffleCost = 0;
			raffling = false;
			return true;
		}
		return false;
	}
	
	public boolean joinRaffle(KittyUser user)
	{
		if(!raffleUsers.containsKey(user) && user.GetBeans() > raffleCost && raffling)
		{
			user.ChangeBeans(raffleCost);
			raffleUsers.put(user, true);
			return true;
		}
		return false; 
	}
	
	public KittyUser chooseRaffleWinner()
	{
		if(!raffleUsers.isEmpty())
		{
			KittyUser chosen = (KittyUser) raffleUsers.keySet().toArray()[new Random().nextInt(raffleUsers.keySet().toArray().length)];
			for(int i = 0; i < 10; i ++)
			{
				if(raffleUsers.get(chosen))
				{
					raffleUsers.replace(chosen, false);
					return chosen;
				}
			}
			
		}
		return null;
	}
	
	@Override
	public String Serialize() 
	{
		return commandIndicator;
	}

	@Override
	public void DeSerialzie(String string) 
	{
		if(string == null || string.length() == 0)
			SetCommandIndicator("!");
		else
			SetCommandIndicator(string);
	}
	
	public String GetCommandIndicator()
	{
		return commandIndicator;
	}
	
	public void SetCommandIndicator(String newIndicator)
	{
		if(newIndicator != commandIndicator)
		{
			commandIndicator = newIndicator;
			MarkDirty();
		}
	}
}
