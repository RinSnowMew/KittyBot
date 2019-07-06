package core;

import java.util.ArrayList;
import java.util.Vector;

import dataStructures.KittyCharacter;
import dataStructures.KittyTrackedLong;
import dataStructures.KittyUser;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import utils.GlobalLog;
import utils.LogFilter;

public class CharacterManager 
{
	public static CharacterManager instance;
	KittyTrackedLong uniqueID = new KittyTrackedLong("CharacterIDCounter", "");
	
	private Vector <KittyCharacter> characters = new Vector<KittyCharacter>();
	public CharacterManager()
	{
		if(instance == null)
		{
			instance = this;
		}
		else
		{
			GlobalLog.error(LogFilter.Core, "Attempted to create a second CharacterManager singleton!");
			return;
		}
	}
	
	public ArrayList<KittyCharacter> searchCharacter(String query)
	{	
		ArrayList<KittyCharacter> chars = new ArrayList<KittyCharacter>();
		try
		{
			long UID = Long.parseLong(query);
			for(KittyCharacter character:characters)
			{
				if(character.getUID() == UID)
				{
					chars.add(character);
					break;
				}
			}
		}catch(Exception e)
		{
			for(KittyCharacter character:characters)
			{
				if(FuzzySearch.ratio(query.toLowerCase(), character.getName().toLowerCase()) > 60)
				{
					chars.add(character);
				}
			}
		}
		return chars;
	}
	
	public boolean addCharacter(KittyUser user, String name, String bio, String refImage)
	{
		for(KittyCharacter character:characters)
		{
			if(character.getName().equals(name) && character.getOwner().discordID.equals(user.discordID))
			{
				return false; 
			}
		}
		
		characters.add(new KittyCharacter(user, name, bio, refImage, uniqueID.get()));
		uniqueID.add(1);
		return true; 
	}
	
	public void removeCharacter(KittyCharacter character, String name)
	{
		characters.remove(character);
	}
	
	public void editRefImage(KittyCharacter character, String refImage)
	{
		character.editRefImage(refImage);
	}
	
	public void editBio(KittyCharacter character, String bio)
	{
		character.editBio(bio);
	}

	public void editName(KittyCharacter character, String name) 
	{
		character.editName(name);
	}
	
}
