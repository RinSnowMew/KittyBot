package commands.characters;

import core.CharacterManager;
import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.*;

public class SubCommandCreate extends SubCommand
{
	public SubCommandCreate(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		String [] info = input.split(",");
		if(info.length < 3)
		{
			return new SubCommandFormattable (LocStrings.stub("CharacterCreateNullInfo"));
		}
		if(CharacterManager.instance.addCharacter(user, info[0], info[1], info[2]))
		{
			return new SubCommandFormattable (LocStrings.stub("CharacterCreateSuccess"));
			
		}
		return new SubCommandFormattable (LocStrings.stub("CharacterCreateDuplicate"));
		}
}