package commands.general;

import java.awt.Color;

import core.Command;
import core.LocStrings;
import dataStructures.KittyChannel;
import dataStructures.KittyEmbed;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandCrouton extends Command
{
	public CommandCrouton(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String getHelpText() { return LocStrings.stub("CroutonInfo"); };
	
	@Override
	public void onRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		KittyEmbed embed = new KittyEmbed();
		embed.authorImage = "https://crouton.net/crouton.png";
		embed.authorLink = "https://crouton.net/";
		embed.authorText = "Crouton";
		embed.color = new Color(255f / 255, 153f / 255, 51f / 255); // Crouton
		embed.descriptionText = "Crouton";
		embed.footerText = "Crouton";
		embed.title = "Crouton";
		
		embed.imageURL = "https://crouton.net/crouton.png";
		
		res.send(embed);
	}
}

