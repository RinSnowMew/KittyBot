package commands.mod;

import core.Command;
import core.LocStrings;
import core.SubCommandFramework;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandModMain extends Command
{
	SubCommandFramework framework = new SubCommandFramework();
	public CommandModMain(KittyRole level, KittyRating rating) 
	{ 
		super(level, rating);
		framework.addCommand("kick", new SubCommandKick(KittyRole.Admin, KittyRating.Safe));
		framework.addCommand("mute", new SubCommandMute(KittyRole.Admin, KittyRating.Safe));
		framework.addCommand("ban", new SubCommandBan(KittyRole.Admin, KittyRating.Safe));
	}
	
	@Override
	public String getHelpText() { return LocStrings.stub("ModInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		framework.run(guild, channel, user, input).Call(res);
	}
}