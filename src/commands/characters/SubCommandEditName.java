package commands.characters;

import core.CharacterManager;
import core.LocStrings;
import core.SubCommand;
import core.SubCommandFormattable;
import dataStructures.KittyChannel;
import dataStructures.KittyCharacter;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;

public class SubCommandEditName extends SubCommand
{
	public SubCommandEditName(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, String input)
	{
		try
		{
			Long.parseLong(input.split(" ")[0]);
		}
		catch(Exception e)
		{
			return new SubCommandFormattable (LocStrings.stub("CharacterEditNameNotValid"));
		}
		KittyCharacter character = CharacterManager.instance.searchCharacter(input.split(" ")[0]).get(0);
		if(character.getOwner().equals(user))
		{
			CharacterManager.instance.editName(character, input.substring(input.indexOf(' ')));
			return new SubCommandFormattable (LocStrings.stub("CharacterEditNameSuccess"));
		}
		else
		{
			return new SubCommandFormattable (LocStrings.stub("CharacterEditNameNotAuth"));
		}
	}
}