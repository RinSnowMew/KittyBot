package commands.music;

import core.Command;
import core.LocStrings;
import core.SubCommandFormattable;
import core.SubCommandFramework;
import dataStructures.*;

public class CommandMusicMain extends Command 
{
	SubCommandFramework framework = new SubCommandFramework();
	public CommandMusicMain(KittyRole level, KittyRating rating) 
	{
		super(level, rating); 
		framework.addCommand("join", new SubCommandJoin(KittyRole.General, KittyRating.Safe));
		framework.addCommand("leave", new SubCommandLeave(KittyRole.General, KittyRating.Safe));
		framework.addCommand("add", new SubCommandAddTrack(KittyRole.General, KittyRating.Safe));
		framework.addCommand("skip", new SubCommandSkip(KittyRole.General, KittyRating.Safe));
		framework.addCommand("stop", new SubCommandStop(KittyRole.General, KittyRating.Safe));
		framework.addCommand("playlist", new SubCommandPlaylist(KittyRole.General, KittyRating.Safe));
	}
	
	@Override
	public String getHelpText() {return LocStrings.stub("MusicInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		SubCommandFormattable scf = framework.run(guild, channel, user, input);
		System.out.println("SCF: " + scf.resString);
		scf.Call(res);
	}
}