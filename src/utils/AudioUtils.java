package utils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class AudioUtils 
{
	private Guild guild;
	public AudioManager audio;
	public final AudioPlayer player;
	
	
	public AudioUtils(Guild guild)
	{
		this.guild = guild;
		audio = guild.getAudioManager();
		player = AudioController.instance.createPlayer();
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
