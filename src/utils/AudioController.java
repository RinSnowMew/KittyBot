package utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

public class AudioController 
{
	public static AudioController instance;
	AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	
	public AudioController ()
	{
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			AudioSourceManagers.registerRemoteSources(playerManager);
			return;
		}
	}
	
	public AudioPlayer createPlayer()
	{
		return playerManager.createPlayer();
	}
}