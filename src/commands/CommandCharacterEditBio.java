package commands;

import core.CharacterManager;
import core.Command;
import core.LocStrings;
import dataStructures.*;

public class CommandCharacterEditBio extends Command
{
	public CommandCharacterEditBio(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return LocStrings.Stub("CharacterEditBioInfo"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		try
		{
			Long.parseLong(input.args.split(" ")[0]);
		}
		catch(Exception e)
		{
			res.Call(LocStrings.Stub("CharacterEditBioNotValid"));
			return;
		}
		KittyCharacter character = CharacterManager.instance.searchCharacter(input.args.split(" ")[0]).get(0);
		if(character.getOwner().equals(user))
		{
			CharacterManager.instance.editBio(character, input.args.substring(input.args.indexOf(' ')));
			res.Call(LocStrings.Stub("CharacterEditBioSuccess"));
		}
		else
		{
			res.Call(LocStrings.Stub("CharacterEditBioNotAuth"));
		}
	}
}