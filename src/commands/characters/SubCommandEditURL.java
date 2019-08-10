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
import dataStructures.UserInput;

public class SubCommandEditURL extends SubCommand
{
	public SubCommandEditURL(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		try
		{
			Long.parseLong(input.args.split(" ")[0]);
		}
		catch(Exception e)
		{
			return new SubCommandFormattable (LocStrings.stub("CharacterEditURLNotValid"));
		}
		KittyCharacter character = CharacterManager.instance.searchCharacter(input.args.split(" ")[0]).get(0);
		if(character.getOwner().equals(user))
		{
			CharacterManager.instance.editRefImage(character, input.args.substring(input.args.indexOf(' ')));
			return new SubCommandFormattable (LocStrings.stub("CharacterEditURLSuccess"));
		}
		else
		{
			return new SubCommandFormattable (LocStrings.stub("CharacterEditURLNotAuth"));
		}
	}
}