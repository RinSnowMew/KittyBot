package dataStructures;

import java.io.File;
import java.io.InputStream;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
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
	private final int discordMessageMax = 2000;
	private final int kittyMessageMax = 1950;
	private JDA kitty;
	
	// Constructor
	public Response(GuildMessageReceivedEvent event, KittyCore kitty)
	{
		this.event = event; 
		this.kitty = kitty.jda;
	}
	
	// Builds a nicely formatted embedded message based on information provided
	public void send(KittyEmbed embedInfo)
	{
		EmbedBuilder embed = new EmbedBuilder();
		
		if(embedInfo.title != null && !embedInfo.title.isEmpty())
		{
			embed.setTitle(embedInfo.title);
		}
		else
		{
			// We have to set the title to at least something that's not empty.
			// By defualt, this creates nothing really visible.
			embed.setTitle(" ");
		}
		
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
		
		if(embedInfo.thumbnailURL != null && embedInfo.thumbnailURL.contains("attachment://"))
		{
			// This is the case where you have a thumbnail image, but you want it to be a local file.
			// TODO: Make this more generic. Consider message builder for the entire thing.
			String thumbPath = embedInfo.thumbnailURL;
			String thumbName = thumbPath.replace("attachment://", "");
			
			// Build the message and send if the file is valid
			MessageBuilder message = new MessageBuilder();
			embed.setThumbnail(thumbPath);
			message.setEmbed(embed.build());
			
			File thumbImage = new File(thumbName);
			if(thumbImage.exists())
			{
				event.getChannel().sendFile(thumbImage, thumbName, message.build()).queue();
			}
		}
		else
		{
			if(embedInfo.thumbnailURL != null)
				embed.setThumbnail(embedInfo.thumbnailURL);
			
			event.getChannel().sendMessage(embed.build()).queue();
		}
	}
	
	// Queues a standard text-based message response to the channel that issued the command.
	public void send(String toRespondWith)
	{
		GlobalLog.log(LogFilter.Response, "Sending response: " + toRespondWith);
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
	public void sendToChannel(String toRespondWith, String channelID)
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
	public void sendImmediate(String toRespondWith)
	{
		GlobalLog.log(LogFilter.Response, "Sending immediate response: " + toRespondWith);
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
	public void sendFile(File toRespondWith, String extension)
	{
		GlobalLog.log(LogFilter.Response, "Sending file response");
		event.getChannel().sendFile(toRespondWith, "return." + extension).queue();
	}

	// Queues an input stream response to the channel that issued the command.
	public void sendFile(InputStream in, String extension) 
	{
		GlobalLog.log(LogFilter.Response, "Sending input stream response");
		event.getChannel().sendFile(in, "return." + extension).queue();
	}
}
