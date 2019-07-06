package dataStructures;

import java.util.List;

import core.ObjectBuilderFactory;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class UserInput 
{
	// Variables
	public String key;
	public String args;
	public KittyUser [] mentions;
	private boolean isValid;
	public String message;
	
	// NOTE(wisp): Designed to parse a string as it's constructed.
	// If a user enters the command "!ping some stuff here :D"
	// and if the command indicator is !, then...
	//
	// key=ping
	// args=some stuff here :D
	//
	// The CommandIndicator and spaces between the key and args are dropped.
	public UserInput(GuildMessageReceivedEvent event, KittyGuild guildContext)
	{
		String toParse = event.getMessage().getContentRaw();
		message = toParse; 
		
		key = "";
		args = "";
		
		if(toParse.length() <= 0)
			return;

		if(!event.getMessage().getMentionedMembers().isEmpty())
			mentions = findMentionedUsers(event);
		
		toParse = toParse.trim();
		String commandIndicator = guildContext.getCommandIndicator();
		if(toParse.startsWith(commandIndicator))
		{
			int loc = findFirstWhitespace(toParse);
			
			if(loc <= 0)
			{
				key = toParse.substring(commandIndicator.length()).trim().toLowerCase();
			}
			else
			{
				key = toParse.substring(commandIndicator.length(), loc).trim().toLowerCase();
				args = toParse.substring(loc).trim();
			}
			
			isValid = true;
		}
		else
		{
			isValid = false;
		}
	}
	
	// Used for injecting direct commands from plugins
	public UserInput(String input, KittyGuild contextGuild)
	{
		int loc = findFirstWhitespace(input);
		key = input.substring(0, loc);
		args = input.substring(loc);
		isValid = true;
	}

	public boolean isValid()
	{
		return isValid;
	}
	
	// Finds first whitespace in the string
	private int findFirstWhitespace(String str)
	{
		for (int i = 0; i < str.length(); ++i) 
		{
			if (Character.isWhitespace(str.charAt(i)))
				return i;
		}
	
		return -1;
	}
	
	private KittyUser[] findMentionedUsers(GuildMessageReceivedEvent event) 
	{
		List <Member> JDAMentions = event.getMessage().getMentionedMembers();
		KittyUser [] KittyMentions = new KittyUser[JDAMentions.size()];
		for(int i = 0; i < KittyMentions.length; i ++)
		{
			KittyMentions [i] = ObjectBuilderFactory.getKittyUser(event.getGuild().getId(), JDAMentions.get(i).getUser().getId());
		}
		return KittyMentions;
	}
}
