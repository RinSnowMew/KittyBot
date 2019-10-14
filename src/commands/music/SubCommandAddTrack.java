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
		
		String response = YT.getYT(input.args);
		return new SubCommandFormattable(response);
	}
}