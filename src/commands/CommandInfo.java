package commands;

import core.Command;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

public class CommandInfo extends Command 
{
	public CommandInfo(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return "Provides author info and a link to Kitty's website"; }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String info = "I'm made by `Rin#8904` and `Reverie Wisp#3703`!\n"
				+ "You can find more info about me along with a Patreon link to support us and GitHub link for filing bugs https://www.kittybot.net" ;
		res.Call(info);
	}
}