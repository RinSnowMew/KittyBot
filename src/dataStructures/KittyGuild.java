package dataStructures;

import java.util.ArrayList;

import core.DatabaseManager;
import core.DatabaseTrackedObject;
import utils.AdminControl;
import utils.AudioUtils;

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
	public AudioUtils audio; 
	public ArrayList <KittyUser> raffleUsersUnchosen = new ArrayList<KittyUser>();
	public ArrayList <KittyUser> raffleUsersChosen = new ArrayList<KittyUser>();
	
	// Database synced info
	private final String roleListName = "guildRoles";
	public final KittyTrackedVector roleList;
	
	private final String beansName = "guildBeans";
	public final KittyTrackedLong beans;
	
	private String commandIndicator;
	
	private void registerTrackedObjects()
	{
		DatabaseManager.instance.globalRegister(roleList);
		DatabaseManager.instance.globalRegister(beans);
	}
	
	// Default content for a guild
	public KittyGuild(String uniqueID, AdminControl adminControl, ArrayList <String> emoji)
	{
		super(uniqueID);
		this.uniqueID = uniqueID;
		roleList = new KittyTrackedVector(roleListName, uniqueID);
		beans = new KittyTrackedLong(beansName, uniqueID);
		registerTrackedObjects();
		
		control = adminControl;
		this.contentRating = KittyRating.Safe; 
		this.polling = false;
		this.emoji = emoji;
		setCommandIndicator("!");
	}
	
	// Explicit constructor
	public KittyGuild(String commandIndicator, KittyRating contentRating, KittyUser guildOwner, String uniqueID)
	{
		super(uniqueID);
		this.uniqueID = uniqueID;
		roleList = new KittyTrackedVector(roleListName, uniqueID);
		beans = new KittyTrackedLong(beansName, uniqueID);
		registerTrackedObjects();
		
		setCommandIndicator(commandIndicator);
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
			raffleUsersChosen.removeAll(raffleUsersChosen);
			raffleUsersUnchosen.removeAll(raffleUsersUnchosen);
			return true;
		}
		return false;
	}
	
	public boolean joinRaffle(KittyUser user)
	{
		if(!raffleUsersChosen.contains(user) && !raffleUsersUnchosen.contains(user) && user.getBeans() > raffleCost && raffling)
		{
			user.changeBeans(raffleCost);
			raffleUsersUnchosen.add(user);
			return true;
		}
		return false; 
	}
	
	public KittyUser chooseRaffleWinner()
	{
		if(!raffleUsersUnchosen.isEmpty())
		{
			KittyUser chosen = raffleUsersUnchosen.get((int) (Math.random() * raffleUsersUnchosen.size()));
			raffleUsersUnchosen.remove(chosen);
			raffleUsersChosen.add(chosen);
			return chosen;
		}
		return null;
	}
	
	@Override
	public String serialize() 
	{
		return commandIndicator;
	}

	@Override
	public void deSerialzie(String string) 
	{
		if(string == null || string.length() == 0)
			setCommandIndicator("!");
		else
			setCommandIndicator(string);
	}
	
	public String getCommandIndicator()
	{
		return commandIndicator;
	}
	
	public void setCommandIndicator(String newIndicator)
	{
		if(newIndicator != commandIndicator)
		{
			commandIndicator = newIndicator;
			markDirty();
		}
	}
}
