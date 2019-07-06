package commands.characters;

import commands.guildrole.*;
import core.Command;
import core.LocStrings;
import core.SubCommandFramework;
import dataStructures.*;

public class CommandCharacterMain extends Command
{
	SubCommandFramework framework = new SubCommandFramework();
	public CommandCharacterMain(KittyRole level, KittyRating rating) 
	{ 
		super(level, rating); 
		framework.addCommand("add", new SubCommandAdd(KittyRole.General, KittyRating.Safe));
		framework.addCommand("editbio", new SubCommandEditBio(KittyRole.General, KittyRating.Safe));
		framework.addCommand("editname", new SubCommandEditName(KittyRole.General, KittyRating.Safe));
		framework.addCommand("editurl", new SubCommandEditURL(KittyRole.General, KittyRating.Safe));
		framework.addCommand("search", new SubCommandSearch(KittyRole.General, KittyRating.Safe));
	}
	
	@Override
	public String getHelpText() { return LocStrings.stub("CharacterInfo"); }
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		framework.run(guild, channel, user, input.args).Call(res);
	}
}