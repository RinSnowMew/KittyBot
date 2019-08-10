package commands.raffle;

import core.Command;
import core.LocStrings;
import core.SubCommandFramework;
import dataStructures.*;

public class CommandRaffleMain extends Command 
{
	SubCommandFramework framework = new SubCommandFramework();
	public CommandRaffleMain(KittyRole level, KittyRating rating) 
	{ 
		super(level, rating); 
		framework.addCommand("start", new SubCommandStart(KittyRole.Mod, KittyRating.Safe));
		framework.addCommand("spin", new SubCommandSpin(KittyRole.Mod, KittyRating.Safe));
		framework.addCommand("end", new SubCommandEnd(KittyRole.Mod, KittyRating.Safe));
		framework.addCommand("join", new SubCommandJoin(KittyRole.General, KittyRating.Safe));
	}
	
	@Override
	public String getHelpText() { return LocStrings.stub("RaffleInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res) 
	{
		framework.run(guild, channel, user, input).Call(res);
	}
}