package dataStructures;

import java.io.File;
import java.io.InputStream;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.*;
import net.dv8tion.jda.core.EmbedBuilder;
import utils.GlobalLog;
import utils.LogFilter;

// This isn't constructed with the factory at this time, mostly because we need it 
// to handle all the response queue behavior and everything internally. So long as 
// commands aren't exposed to the GuildMessageReceivedEvent then we should be fine.
public class Response 
{
	// Variables
	private GuildMessageReceivedEvent event;
	private JDA kitty;
	private final int discordMessageMax = 2000;
	private final int kittyMessageMax = 1950;
	
	// Constructor
	public Response(GuildMessageReceivedEvent event, JDA kitty)
	{
		this.event = event; 
		this.kitty = kitty; 
	}
	
	// Builds a nicely formatted embedded message based on information provided
	public void CallEmbed(KittyEmbed embedInfo)
	{
		EmbedBuilder embed = new EmbedBuilder();
		
		if(embedInfo.title != null)
			embed.setTitle(embedInfo.title);
		
		if(embedInfo.color != null)
			embed.setColor(embedInfo.color);
		
		if(embedInfo.descriptionText != null)
			embed.setDescription(embedInfo.descriptionText);
		
		if(embedInfo.footerText != null)
			embed.setFooter(embedInfo.footerText, null);
		
		if(embedInfo.authorImage != null || embedInfo.authorLink != null || embedInfo.authorText != null)
			embed.setAuthor(embedInfo.authorText, embedInfo.authorLink, embedInfo.authorImage);
		
		if(embedInfo.imageURL != null)
            embed.setImage(embedInfo.imageURL);
		
		event.getChannel().sendMessage(embed.build()).queue();
	}
	
	// Queues a standard text-based message response to the channel that issued the command.
	public void Call(String toRespondWith)
	{
		GlobalLog.Log(LogFilter.Response, "Sending response: " + toRespondWith);
		if(toRespondWith.length() > discordMessageMax)
		{
			event.getChannel().sendMessage(toRespondWith.substring(0, kittyMessageMax) + "\n\nI think that's enough!").queue();
		}
		else
		{
			event.getChannel().sendMessage(toRespondWith).queue();
		}
	}
	
	// Queues a standard text-based message response to the specified channel.
	public void CallToChannel(String toRespondWith, String channelID)
	{
		TextChannel channel;
		channel = kitty.getTextChannelById(Long.parseLong(channelID));
		if(toRespondWith.length() > discordMessageMax)
		{
			channel.sendMessage(toRespondWith.substring(0, kittyMessageMax) + "\n\nI think that's enough!").queue();
		}
		else
		{
			channel.sendMessage(toRespondWith).queue();
		}
	}
	
	// Immediately dispatches the message to the channel that issued the command.
	public void CallImmediate(String toRespondWith)
	{
		GlobalLog.Log(LogFilter.Response, "Sending immediate response: " + toRespondWith);
		if(toRespondWith.length() > discordMessageMax)
		{
			event.getChannel().sendMessage(toRespondWith.substring(0, kittyMessageMax) + "\n\nI think that's enough!");
		}
		else
		{
			event.getChannel().sendMessage(toRespondWith).complete();
		}
	}
	
	// Queues a file response to the channel that issued the command.
	public void CallFile(File toRespondWith, String extension)
	{
		GlobalLog.Log(LogFilter.Response, "Sending file response");
		event.getChannel().sendFile(toRespondWith, "return." + extension).queue();
	}

	// Queues an input stream response to the channel that issued the command.
	public void CallInput(InputStream in, String extension) 
	{
		GlobalLog.Log(LogFilter.Response, "Sending input stream response");
		event.getChannel().sendFile(in, "return." + extension).queue();
	}
}
