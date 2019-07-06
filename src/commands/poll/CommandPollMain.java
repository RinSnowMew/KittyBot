package commands.poll;

import core.Command;
import core.SubCommandFramework;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandPollMain extends Command 
{
	SubCommandFramework framework = new SubCommandFramework();
	public CommandPollMain(KittyRole level, KittyRating rating) 
	{ 
		super(level, rating); 
		
		framework.addCommand("start", new SubCommandStart(KittyRole.Mod, KittyRating.Safe));
		framework.addCommand("addoption", new SubCommandAddOption(KittyRole.Mod, KittyRating.Safe));
		framework.addCommand("end", new SubCommandEnd(KittyRole.Mod, KittyRating.Safe));
		
		framework.addCommand("show", new SubCommandShow(KittyRole.General, KittyRating.Safe));
		framework.addCommand("results", new SubCommandResults(KittyRole.General, KittyRating.Safe));
		framework.addCommand("vote", new SubCommandVote(KittyRole.General, KittyRating.Safe));	
	}
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res) 
	{
		framework.run(guild, channel, user, input.args).Call(res);
	}
}
