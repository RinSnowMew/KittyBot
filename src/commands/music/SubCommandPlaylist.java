package commands.music;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.*;

public class SubCommandPlaylist extends SubCommand 
{

	public SubCommandPlaylist(KittyRole roleLevel, KittyRating contentRating) {super(roleLevel, contentRating);	}

	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input) 
	{
//		ArrayList<AudioTrack> playlist = guild.audio.getPlaylist();
		String stringPlaylist = guild.audio.getPlaylist(guild.audio.player);
		return new SubCommandFormattable(stringPlaylist);
	}

}
