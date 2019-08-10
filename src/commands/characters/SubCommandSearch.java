package commands.characters;

import java.util.ArrayList;

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

public class SubCommandSearch extends SubCommand
{
	public SubCommandSearch(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public SubCommandFormattable OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input)
	{
		ArrayList <KittyCharacter> characters = CharacterManager.instance.searchCharacter(input.args);
		if(characters.size() < 1)
		{
			return new SubCommandFormattable (LocStrings.stub("CharacterSearchNoCharacterFound"));
		}
		String formatted = "";
		if(characters.size() > 1)
		{
			formatted += (LocStrings.stub("CharacterSearchMultipleCharacterHeader"));
			for(KittyCharacter character:characters)
			{
				formatted += "\n" + (String.format(LocStrings.stub("CharacterSearchMultipleCharacter"), character.getName(), character.getOwner().name, character.getUID()));
			}
			return new SubCommandFormattable(formatted);
		}
		else
		{
			return new SubCommandFormattable (String.format(LocStrings.stub("CharacterSearchOneCharacter"), characters.get(0).getOwner().name, characters.get(0).getName(), characters.get(0).getBio(), characters.get(0).getRefImage(), characters.get(0).getUID()));
		}
	}
}