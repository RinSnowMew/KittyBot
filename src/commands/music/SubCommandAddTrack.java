package commands.music;

import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.UserInput;
import network.NetworkYoutube;

public class SubCommandAddTrack extends SubCommand 
{
	
	public SubCommandAddTrack(KittyRole level, KittyRating rating) { super(level, rating); }
	
	NetworkYoutube YT = new NetworkYoutube();
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		//Get track from Youtube 
		String video = input.args;
		if(!(video.startsWith("https://www.youtube") || video.startsWith("https://www.youtu.be")))
		{
			video =  YT.getYT(video);
		}
		guild.audio.playVideo(video);
		return new SubCommandFormattable(video);
	}
}