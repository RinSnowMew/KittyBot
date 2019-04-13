package commands;

import core.Command;
import core.Localizer;
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
	public String HelpText() { return Localizer.Stub("Provides author info and a link to Kitty's website"); }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{
		String info = Localizer.Stub("I'm made by `Rin#8904` and `Reverie Wisp#3703`!\nYou can find more info about me along with a Patreon link to support us and GitHub link for filing bugs https://www.rinsnowmew.com/bot/");
		res.Call(info);
	}
}