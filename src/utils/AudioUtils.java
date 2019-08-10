package utils;
/***
 * https://developers.google.com/youtube/v3/code_samples/code_snippets?apix_params=%7B%22part%22%3A%22snippet%22%2C%22maxResults%22%3A25%2C%22q%22%3A%22surfing%22%7D
 * https://github.com/sedmelluq/LavaPlayer#jda-integration
 * https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/core/entities/VoiceChannel.html
 * https://developers.google.com/youtube/registering_an_application#Create_API_Keys
 */
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class AudioUtils 
{
	private Guild guild;
	AudioManager audio;
	AudioPlayer player;
	
	
	public AudioUtils(Guild guild)
	{
		this.guild = guild;
		audio = guild.getAudioManager();
	}
	
	public boolean joinChannel(String channelName)
	{
		VoiceChannel channel = null; 
		channel = guild.getVoiceChannelsByName(channelName, true).get(0);
		if(channel != null)
		{
			audio.openAudioConnection(channel);
			return true;
		}
		return false; 
	}
	
	public boolean leaveChannel()
	{
		if(audio.isConnected())
		{
			audio.closeAudioConnection();
			return true;
		}
		return false;
	}
}
