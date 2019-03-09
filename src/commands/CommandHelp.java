package commands;

import core.*;
import dataStructures.KittyChannel;
import dataStructures.KittyGuild;
import dataStructures.KittyRating;
import dataStructures.KittyRole;
import dataStructures.KittyUser;
import dataStructures.Response;
import dataStructures.UserInput;

// TODO: Convert this to a per-command help string! This lets us do all sorts of neat stuff,
// mostly tho it lets us construct this on the fly or by hand for a specific command!
public class CommandHelp extends Command
{
	public CommandHelp(KittyRole level, KittyRating rating) { super(level, rating); }
	
	@Override
	public String HelpText() { return "Lets you look up specific commands, or get a link to a list of all commands."; }
	
	@Override
	public void OnRun(KittyGuild guild, KittyChannel channel, KittyUser user, UserInput input, Response res)
	{ 
		String help = Stats.instance.GetHelpText(input.args);
		
		if(help == null)
		{
			help = "You can get help with a specific command by typing `!help command`!"
					+ "\nYou can also look at <https://www.kittybot.net/info/#commands>"
					+ "\nGeneral Commands: `boop, roll, choose, help, info, vote, "
					+ "results, showpoll, wolfram, cplus, java,"
					+ "beans, role, derp, e621, bet, yeet`";
		}
		else
		{
			help = "`" + input.args + "`: " + help;
		}
		
		res.Call(help);
	}
	
}