package dataStructures;

import core.DatabaseTrackedObject;
import utils.GlobalLog;
import utils.LogFilter;

// NOTE(wisp): A user is a specific instance of a discord user on a given
// discord server. This means that if a user is on two servers, they have
// two unique user objects associated with them.
public class KittyUser extends DatabaseTrackedObject
{
	public KittyGuild guild; // The guild this user is associated with. A KittyUser can not be associated with more than one guild.
	public String name;      // The 'friendly name' of the user (readable name).
	public String uniqueID;  // The combination of the guild ID that discord uses and the user-specific ID discord uses. (A discord account has one user per server).
	public String discordID; // The user-specific ID only. This is the ID associated with the discord account.
	public String avatarID;  // The searchable ID of the current avatar image for the user.
	private KittyRole role;  // The command permissions role this user has for issuing bot commands for this bot.
	private long beans;      // A value incremented each time a user sends a message in a server they're in.
	
	// Explicit Constructor
	public KittyUser(String name, KittyGuild guild, KittyRole role, String uniqueID, String avatarID, String discordID)
	{
		super(uniqueID);
		this.name = name;
		this.role = role;
		this.guild = guild;
		this.uniqueID = uniqueID;
		this.beans = 0;
		this.avatarID = avatarID;
		this.discordID = discordID;
	}

	// Can be positive or negative
	public void changeBeans(int amount)
	{
		beans += amount;
		
		if(amount != 0)
			markDirty();
	}
	
	public void changeRole(KittyRole newRole)
	{
		if(newRole != role)
		{
			role = newRole;
			markDirty();
		}
	}
	
	public long getBeans()
	{
		return beans;
	}
	
	public KittyRole getRole()
	{
		return role;
	}
	
	@Override
	public String serialize() 
	{
		return beans + "," + role.getValue();
	}

	public static String[] prepareFromString(String string)
	{
		return string.split(",");
	}
	
	public static long parseBeans(String[] prepared)
	{
		return Integer.parseInt(prepared[0]);
	}
	
	public static KittyRole parseRole(String[] prepared)
	{
		return KittyRole.valueOf(Integer.parseInt(prepared[1])).get();
	}
	
	@Override
	public void deSerialzie(String string) 
	{
		try
		{
			String[] strings = prepareFromString(string);
			if(strings.length < 2)
			{
				GlobalLog.log(LogFilter.Database, "Upgrading user " + name + " to include 'role' in DB");
				
				// Mark ourselves dirty to re-write the role information stored in the user.
				// Just uses defaults from earlier again.
				markDirty();
			}
			else
			{
				beans = parseBeans(strings);
				role = parseRole(strings);
			}
		}
		catch (NumberFormatException e)
		{
			GlobalLog.warn(LogFilter.Database, "Invalid user data for user " + name + "! "
					+ "Starting over at 0 beans with a general role!");
			
			// We don't need to specify the role at this point because it is set at this point.
			// We use what the user was created with whatever defaults were in the factory. 
			// Beans are maintained too, just in case there's some in cache.
			markDirty();
		}
	}
}
