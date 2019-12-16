package utils.audio;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class AudioUtils 
{
	private Guild guild;
	public AudioManager am;
	public final AudioPlayer player;
	public final AudioPlayerManager playerManager;
	private Queue <AudioTrack> playlist = new LinkedList<>();
	
	
	public AudioUtils(Guild guild, AudioPlayerManager playerManager)
	{
		player = playerManager.createPlayer();
		this.playerManager = playerManager;
		this.guild = guild;
		am = guild.getAudioManager();
		
	}
	
	public boolean joinChannel(String channelName)
	{
		VoiceChannel channel = null; 
		channel = guild.getVoiceChannelsByName(channelName, true).get(0);
		if(channel != null)
		{
			am.openAudioConnection(channel);
			return true;
		}
		return false; 
	}
	
	public boolean leaveChannel()
	{
		if(am.isConnected())
		{
			am.closeAudioConnection();
			return true;
		}
		return false;
	}
	
	
	public class AudioPlayerSendHandler implements AudioSendHandler 
	{
		private final AudioPlayer audioPlayer;
		private AudioFrame lastFrame;
		
		public AudioPlayerSendHandler(AudioPlayer audioPlayer) 
		{
			this.audioPlayer = audioPlayer;
		}
		
		@Override
		public boolean canProvide() 
		{
			lastFrame = audioPlayer.provide();
			return lastFrame != null;
		}
		
		@Override public byte[] provide20MsAudio()
		{
			return lastFrame.getData();
		}
		
		@Override public boolean isOpus()
		{
			return true;
		}
	}
	
	public class SmallAudio implements AudioLoadResultHandler
	{
		private AudioPlayer player;
		
		private SmallAudio(AudioPlayer player)
		{
			this.player = player;
		}
		
		@Override public void loadFailed(FriendlyException arg0) { }
		@Override public void noMatches() { }
		@Override public void playlistLoaded(AudioPlaylist arg0) { }
		
		@Override
		public void trackLoaded(AudioTrack track)
		{
			synchronized(playlist)
			{
				playlist.add(track);
			}
			do 
			{
				if(player.getPlayingTrack() == null)
				{
					player.startTrack(playlist.poll(), false);
				}
			}
			while(!playlist.isEmpty());
		}
	}
	
	
	public String playVideo(String videoURL)
	{
		guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		playerManager.loadItemOrdered(player, videoURL, new SmallAudio(player));
		
		return null;
	}
	
	public String skipVideo(AudioPlayer player)
	{
		return null;
	}
	
	public String stopPlayer(AudioPlayer player)
	{
		playlist.clear();
		player.stopTrack();
		return "Stopped";
	}
	
	public String getPlaylist(AudioPlayer player)
	{
		return player.getPlayingTrack().getInfo().title;
	}
}
